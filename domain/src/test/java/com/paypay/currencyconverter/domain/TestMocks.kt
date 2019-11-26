package com.paypay.currencyconverter.domain

import android.icu.util.Currency
import android.icu.util.CurrencyAmount
import com.paypay.currencyconverter.domain.entities.ExchangeRates
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit

val now = ZonedDateTime.now()
val retrievedAt = now.truncatedTo(ChronoUnit.SECONDS)
val jpy = Currency.getInstance("JPY")
val cad = Currency.getInstance("CAD")
val rub = Currency.getInstance("RUB")
val usd = Currency.getInstance("USD")
val usdJpyRate = 108.76904
val usdCadRate = 1.322315
val usdRubRate = 63.748038
val usdUsdRate = 1.0
val mockRates = ExchangeRates(
    retrievedAt = retrievedAt,
    rates = mapOf(
        jpy.currencyCode to CurrencyAmount(usdJpyRate, jpy),
        cad.currencyCode to CurrencyAmount(usdCadRate, cad),
        rub.currencyCode to CurrencyAmount(usdRubRate, rub),
        usd.currencyCode to CurrencyAmount(usdUsdRate, usd)
    )
)