package com.paypay.currencycoverter.data.repositories.remote

import android.icu.util.Currency
import com.paypay.currencyconverter.domain.datasources.ExchangeRatesDataSource
import com.paypay.currencyconverter.domain.entities.ExchangeRates
import com.paypay.currencycoverter.data.repositories.remote.mappers.RemoteExchangeRatesMapper
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRatesRemoteDataSource(networkModule: NetworkModule = NetworkModule) : ExchangeRatesDataSource {
    @Inject
    constructor() : this(NetworkModule)

    private val api = networkModule.currencyConverterApi
    private val mapper = RemoteExchangeRatesMapper()

    override fun save(exchangeRates: ExchangeRates) {
        TODO("not needed")
    }

    override fun retrieve(sourceCurrency: Currency?): ExchangeRates? {
        TODO("not needed")
    }

    override fun observe(sourceCurrency: Currency?): Observable<ExchangeRates> {
        // first try to fetch rates with the specified source currency, and if fails, then fall back on default USD
        return api.fetchRates(sourceCurrency?.currencyCode)
            .subscribeOn(Schedulers.io())
            .map { fetchedRates ->
                if (fetchedRates.success) fetchedRates
                else {
                    fetchedRates.error?.let {
                        throw Exception("Code: ${it.code} \nError: ${it.info}")
                    } ?: throw Exception("Couldn't fetch rates from server")
                }
            }
            .onErrorResumeNext { t: Throwable ->
                println(t)
                Timber.w(t)
                api.fetchRates() // this call can fail too, so in production there should be fallbacks for this
            }
            .map { mapper.reverseMap(it) }
            .doOnError { Timber.w(it) }
            .toObservable()
    }
}