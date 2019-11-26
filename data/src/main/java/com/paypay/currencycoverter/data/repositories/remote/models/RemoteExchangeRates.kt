package com.paypay.currencycoverter.data.repositories.remote.models

import com.squareup.moshi.JsonClass
import org.threeten.bp.ZonedDateTime

@JsonClass(generateAdapter = true)
data class RemoteExchangeRates(
    val success: Boolean = false,
    val error: CurrencyFetchError? = null,
    val timestamp: Long? = 0, // not really useful in this app
    val source: String? = "",
    val quotes: Map<String, Double>? = emptyMap(),
    @Transient val retrievedAt: ZonedDateTime = ZonedDateTime.now()
)