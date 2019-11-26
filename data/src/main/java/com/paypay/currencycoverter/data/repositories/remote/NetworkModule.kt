package com.paypay.currencycoverter.data.repositories.remote

import com.paypay.currencycoverter.data.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkModule {
    internal const val BASE_URL = BuildConfig.SERVER_URL

    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        val interceptor = HttpLoggingInterceptor()
        with(interceptor) {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }
        interceptor
    }

    internal val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val newUrl = originalRequest.url.newBuilder()
                    .addQueryParameter("access_key", BuildConfig.ACCESS_KEY)
                    .build()

                val newRequest = originalRequest.newBuilder().url(newUrl)
                chain.proceed(newRequest.build())
            }
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val converter = MoshiConverterFactory.create().asLenient()
    private val callAdapter = RxJava2CallAdapterFactory.create()

    internal val currencyConverterApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addCallAdapterFactory(callAdapter)
        .addConverterFactory(converter)
        .build()
        .create(CurrencyConverterApi::class.java)
}