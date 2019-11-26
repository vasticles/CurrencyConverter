package com.paypay.currencycoverter.data.repositories.cache.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class CacheExchangeRate(
    @PrimaryKey var currenciesFromTo: String = "",
    var rate: Double = 0.0
) : RealmObject()