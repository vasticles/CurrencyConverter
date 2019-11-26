package com.paypay.currencycoverter.data.repositories.cache.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.threeten.bp.ZonedDateTime

open class CacheExchangeRates(
    @PrimaryKey var sourceCurrency: String = "USD",
    var retrievedAt: Long = ZonedDateTime.now().toEpochSecond(),
    var rates: RealmList<CacheExchangeRate> = RealmList()
) : RealmObject()