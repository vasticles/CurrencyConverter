package com.paypay.currencyconverter.domain.extensions

import android.icu.util.CurrencyAmount
import com.paypay.currencyconverter.domain.entities.ExchangeRates

fun CurrencyAmount.convert(exchange: ExchangeRates): List<CurrencyAmount> {
    val baseRate = exchange.getCurrencyRateFor(currency)
    return exchange.rates.values.map { currencyRate ->
        this / baseRate * currencyRate
    }
}

operator fun CurrencyAmount.div(amount: CurrencyAmount): CurrencyAmount {
    val newAmount = number.toDouble() / amount.number.toDouble()
    return CurrencyAmount(newAmount, amount.currency)
}

operator fun CurrencyAmount.times(amount: CurrencyAmount): CurrencyAmount {
    val newAmount = number.toDouble() * amount.number.toDouble()
    return CurrencyAmount(newAmount, amount.currency)
}