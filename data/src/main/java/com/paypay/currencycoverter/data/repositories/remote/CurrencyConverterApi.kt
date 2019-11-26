package com.paypay.currencycoverter.data.repositories.remote

import com.paypay.currencyconverter.domain.DEFAULT_SOURCE_CURRENCY
import com.paypay.currencycoverter.data.repositories.remote.models.RemoteExchangeRates
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyConverterApi {

    @GET("/live")
    fun fetchRates(@Query("source") sourceCurrencyCode: String? = DEFAULT_SOURCE_CURRENCY): Single<RemoteExchangeRates>

//    @GET("/convert")
//    fun convert(@Query("from") from: String, @Query("to") to: String, @Query("amount") amount: Double): Single<RemoteCurrencyAmount>
}