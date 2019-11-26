package com.paypay.currencycoverter.data.repositories.cache

import android.icu.util.Currency
import com.paypay.currencyconverter.domain.DEFAULT_SOURCE_CURRENCY
import com.paypay.currencyconverter.domain.datasources.ExchangeRatesDataSource
import com.paypay.currencyconverter.domain.entities.ExchangeRates
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRatesCacheDataSource(private val cacheModule: CacheModule = CacheModule) : ExchangeRatesDataSource {

    @Inject
    constructor() : this(CacheModule)

    override fun save(exchangeRates: ExchangeRates) {
        cacheModule.save(exchangeRates)
    }

    override fun retrieve(sourceCurrency: Currency?): ExchangeRates? {
        val currency = sourceCurrency?.currencyCode ?: DEFAULT_SOURCE_CURRENCY
        return cacheModule.retrieveExchangeRates(currency)
    }

    override fun observe(sourceCurrency: Currency?): Observable<ExchangeRates> {
        TODO("not needed")
    }
}
