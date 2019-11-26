package com.paypay.currencyconverter.domain.entities

import android.icu.util.Currency
import android.icu.util.CurrencyAmount
import org.threeten.bp.Duration
import org.threeten.bp.ZonedDateTime

data class ExchangeRates(
    val sourceCurrency: Currency = Currency.getInstance("USD"),
    val rates: Map<String, CurrencyAmount> = emptyMap(),
    val retrievedAt: ZonedDateTime = ZonedDateTime.now()
) {

    val areValid: Boolean
        get() {
            val now = ZonedDateTime.now()
            val timePassed = Duration.between(retrievedAt, now).toMinutes()
            val expirationTimeMinutes = 30
            return (timePassed < expirationTimeMinutes) and rates.isNotEmpty()
        }

    @Throws(Exception::class)
    fun getCurrencyRateFor(currency: Currency): CurrencyAmount = rates[currency.currencyCode]
        ?: throw Exception("No rate found for $currency")
}