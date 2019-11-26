package com.paypay.currencycoverter.data.repositories.remote.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrencyFetchError(
    val code: Int = 0,
    val info: String = ""
)