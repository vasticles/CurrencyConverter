package com.paypay.currencycoverter.data.repositories.cache.mappers

import android.icu.util.Currency
import android.icu.util.CurrencyAmount
import com.paypay.currencyconverter.domain.entities.ExchangeRates
import com.paypay.currencycoverter.data.repositories.cache.models.CacheExchangeRate
import com.paypay.currencycoverter.data.repositories.cache.models.CacheExchangeRates
import io.realm.RealmList
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

class CacheExchangeRatesMapper {
    fun map(entity: ExchangeRates): CacheExchangeRates {
        return CacheExchangeRates(
            sourceCurrency = entity.sourceCurrency.currencyCode,
            retrievedAt = entity.retrievedAt.toEpochSecond(),
            rates = entity.rates.map { rate ->
                val currencyKey = "${entity.sourceCurrency}${rate.key}"
                CacheExchangeRate(currencyKey, rate.value.number.toDouble())
            }.asRealmList()
        )
    }

    fun reverseMap(model: CacheExchangeRates): ExchangeRates {
        return ExchangeRates(
            sourceCurrency = Currency.getInstance(model.sourceCurrency),
            retrievedAt = model.retrievedAt.let {
                ZonedDateTime.ofInstant(Instant.ofEpochSecond(it), ZoneId.systemDefault())
            },
            rates = model.rates.associate { quote ->
                val currency = Currency.getInstance(quote.currenciesFromTo.removePrefix(model.sourceCurrency))
                currency.currencyCode to CurrencyAmount(quote.rate, currency)
            }
        )
    }

    private fun List<CacheExchangeRate>.asRealmList(): RealmList<CacheExchangeRate> {
        // unfortunately Realm lacks a way to create a list from another list, so this is a workaround
        return RealmList(*this.toTypedArray())
    }
}