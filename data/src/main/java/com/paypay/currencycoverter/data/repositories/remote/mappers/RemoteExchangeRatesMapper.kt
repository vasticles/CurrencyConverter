package com.paypay.currencycoverter.data.repositories.remote.mappers

import android.icu.util.Currency
import android.icu.util.CurrencyAmount
import com.paypay.currencyconverter.domain.entities.ExchangeRates
import com.paypay.currencycoverter.data.repositories.remote.models.RemoteExchangeRates
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

class RemoteExchangeRatesMapper {

    @Throws(Exception::class)
    fun reverseMap(model: RemoteExchangeRates): ExchangeRates {
        if (!model.success) {
            throw Exception("Cannot map rates, because fetch request failed \nCode: ${model.error?.code} \nInfo: ${model.error?.info}")
        } else {
            return ExchangeRates(
                sourceCurrency = Currency.getInstance(model.source),
                retrievedAt = model.retrievedAt,
                rates = model.quotes?.map { rate ->
                    val currency = model.source?.let {
                        Currency.getInstance(rate.key.removePrefix(it))
                    } ?: Currency.getInstance(rate.key.removeRange(0, 3))
                    currency.currencyCode to CurrencyAmount(rate.value, currency)
                }?.toMap() ?: emptyMap()
            )
        }
    }
}