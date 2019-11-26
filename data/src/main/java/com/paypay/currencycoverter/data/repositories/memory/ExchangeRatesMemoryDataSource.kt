package com.paypay.currencycoverter.data.repositories.memory

import android.icu.util.Currency
import com.paypay.currencyconverter.domain.datasources.ExchangeRatesDataSource
import com.paypay.currencyconverter.domain.entities.ExchangeRates
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

// a dedicated memory data source for a single entity is definitely overkill (basically just storing USD rates);
// however, it's part of the repo pattern, and it's important that it lives as a singleton

@Singleton
class ExchangeRatesMemoryDataSource @Inject constructor() : ExchangeRatesDataSource {

    private var exchangeRates = mutableMapOf<String, ExchangeRates>()

    override fun save(exchangeRates: ExchangeRates) {
        val sourceCurrencyCode = exchangeRates.sourceCurrency.currencyCode
        this.exchangeRates[sourceCurrencyCode] = exchangeRates
    }

    override fun retrieve(sourceCurrency: Currency?): ExchangeRates? {
        val sourceCurrencyCode = sourceCurrency?.currencyCode
        Timber.d("Retrieving code for currency: $sourceCurrency")
        return exchangeRates[sourceCurrencyCode]
    }

    override fun observe(sourceCurrency: Currency?): Observable<ExchangeRates> {
        TODO("not needed")
    }
}