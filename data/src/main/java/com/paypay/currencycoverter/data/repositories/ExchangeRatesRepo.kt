package com.paypay.currencycoverter.data.repositories

import android.icu.util.Currency
import com.paypay.currencyconverter.domain.datasources.ExchangeRatesDataSource
import com.paypay.currencyconverter.domain.entities.ExchangeRates
import com.paypay.currencycoverter.data.repositories.cache.ExchangeRatesCacheDataSource
import com.paypay.currencycoverter.data.repositories.memory.ExchangeRatesMemoryDataSource
import com.paypay.currencycoverter.data.repositories.remote.ExchangeRatesRemoteDataSource
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRatesRepo(
    private val memory: ExchangeRatesDataSource = ExchangeRatesMemoryDataSource(),
    private val local: ExchangeRatesDataSource = ExchangeRatesCacheDataSource(),
    private val remote: ExchangeRatesDataSource = ExchangeRatesRemoteDataSource()
) : ExchangeRatesDataSource {

    @Inject
    constructor(
        memoryDS: ExchangeRatesMemoryDataSource,
        localDS: ExchangeRatesCacheDataSource,
        remoteDS: ExchangeRatesRemoteDataSource
    ) : this(
        memory = memoryDS,
        local = localDS,
        remote = remoteDS
    )

    init {
        Timber.d("ExchangeRates repo init")
        refreshCache()
    }

    var fetchFromServer = true

    private fun refreshCache() {
        local.retrieve()?.let { memory.save(it) }
    }

    override fun save(exchangeRates: ExchangeRates) {
        memory.save(exchangeRates)
        local.save(exchangeRates)
    }

    override fun retrieve(sourceCurrency: Currency?): ExchangeRates? {
        return memory.retrieve(sourceCurrency).let { cachedRates ->
            // check if rates in memory can be used, if not then hydrate memory with rates from local database
            if (cachedRates == null || !cachedRates.areValid) {
                local.retrieve(sourceCurrency)?.let { newRates ->
                    memory.save(newRates)
                    newRates
                }
            } else cachedRates
        } ?: ExchangeRates() // this fallback returns an invalid entity, which will cause a server fetch
    }

    override fun observe(sourceCurrency: Currency?): Observable<ExchangeRates> {
        return Observable.fromCallable { retrieve(sourceCurrency) }
            .doOnSubscribe { println("subscribed to repo.observe") }
            .map { localRates ->
                // check if local rates have expired, and if they did then fetch new rates from server
                if (!localRates.areValid) {
                    fetchFromServer = true
                    throw Exception("Local rates are stale. Fetching from server")
                } else localRates
            }
            // for some reason, the specific use of onErrorResumeNext below was failing tests;
            // odd behaviour, especially considering since the onSubscribe log statements didn't show invocations happening out of order
            // very good to know for the future!
//            .onErrorResumeNext(remote.observe(sourceCurrency))
//                .doOnSubscribe { println("subscribed to remote.observe") }

//             this is the correct way of using onErrorResumeNext
            .onErrorResumeNext { t: Throwable ->
                Timber.d(t.message)
                remote.observe(sourceCurrency)
//                    .doOnSubscribe { println("subscribed to remote.observe") }
            }
            .map { remoteRates ->
                // if local rates were valid, make sure they don't get re-saved
                if (fetchFromServer) {
                    save(remoteRates)
                    fetchFromServer = false
                }
                remoteRates
            }
    }
}