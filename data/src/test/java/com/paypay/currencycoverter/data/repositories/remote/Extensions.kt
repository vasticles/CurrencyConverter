package com.paypay.currencycoverter.data.repositories.remote

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

fun InputStream.asJsonString(): String {
    val streamReader = BufferedReader(InputStreamReader(this, "UTF-8"))
    val responseBuilder = StringBuilder()

    streamReader.lines().forEach {
        responseBuilder.append(it)
    }

    return responseBuilder.toString()
}