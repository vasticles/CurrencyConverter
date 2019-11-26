package com.paypay.currencycoverter.data.repositories.cache

import com.paypay.currencyconverter.domain.entities.ExchangeRates
import com.paypay.currencycoverter.data.repositories.cache.mappers.CacheExchangeRatesMapper
import com.paypay.currencycoverter.data.repositories.cache.models.CacheExchangeRates
import io.realm.Realm
import timber.log.Timber

object CacheModule {

    fun init(realm: Realm) {
        Timber.d("Starting realm")
        Realm.setDefaultConfiguration(realm.configuration)
    }

    private val exchangeRatesMapper = CacheExchangeRatesMapper()

    fun save(exchangeRates: ExchangeRates) {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { transaction ->
                val cacheRates = exchangeRatesMapper.map(exchangeRates)
                transaction.insertOrUpdate(cacheRates)
            }
        }
    }

    fun retrieveExchangeRates(sourceCurrencyCode: String): ExchangeRates? {
        Realm.getDefaultInstance().use { realm ->
            return realm.where(CacheExchangeRates::class.java)
                .equalTo("sourceCurrency", sourceCurrencyCode)
                .findFirst()
                ?.let { exchangeRatesMapper.reverseMap(it) }
        }
    }
}