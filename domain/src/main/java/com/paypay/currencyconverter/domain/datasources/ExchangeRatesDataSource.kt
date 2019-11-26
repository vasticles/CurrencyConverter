package com.paypay.currencyconverter.domain.datasources

import android.icu.util.Currency
import com.paypay.currencyconverter.domain.entities.ExchangeRates
import io.reactivex.Observable

interface ExchangeRatesDataSource {
    fun save(exchangeRates: ExchangeRates)
    fun retrieve(sourceCurrency: Currency? = null): ExchangeRates?
    fun observe(sourceCurrency: Currency? = null): Observable<ExchangeRates>
}