package com.paypay.currencycoverter.data.repositories.remote

import com.paypay.currencycoverter.data.repositories.remote.models.RemoteExchangeRates
import io.mockk.mockkObject
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class NetworkModuleTest {
    private lateinit var api: CurrencyConverterApi
    private lateinit var mockServer: MockWebServer

    @Before
    fun setUp() {
        mockkObject(NetworkModule)
        mockServer = MockWebServer()
        mockServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockServer.url("/"))
            .client(NetworkModule.client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
            .build()
        api = retrofit.create(CurrencyConverterApi::class.java)
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }

    @Test
    fun `When fetch call is made, Then the sample json object is returned as a Remote exchange rates model`() {
        val sessionJson =
            javaClass.classLoader?.getResourceAsStream("rates.json")?.asJsonString() ?: ""
        val serverResponse = MockResponse().setResponseCode(200).setBody(sessionJson)

        mockServer.enqueue(serverResponse)

        val remoteExchangeRates = api.fetchRates().blockingGet()
        assertThat(remoteExchangeRates).isInstanceOf(RemoteExchangeRates::class.java)
    }

    @Test
    fun `When fetch succeeds, Then the response's Success property is true, and Quotes property is not empty`() {
        val sessionJson =
            javaClass.classLoader?.getResourceAsStream("rates.json")?.asJsonString() ?: ""
        val serverResponse = MockResponse().setResponseCode(200).setBody(sessionJson)

        mockServer.enqueue(serverResponse)

        val remoteExchangeRates = api.fetchRates().blockingGet()
        assertThat(remoteExchangeRates.success).isTrue()
        assertThat(remoteExchangeRates.quotes).isNotEmpty
    }

    @Test
    fun `When fetch fails, Then the response's Success property is false`() {
        val sessionJson =
            javaClass.classLoader?.getResourceAsStream("error.json")?.asJsonString() ?: ""
        val serverResponse = MockResponse().setResponseCode(200).setBody(sessionJson)

        mockServer.enqueue(serverResponse)

        val remoteExchangeRates = api.fetchRates(sourceCurrencyCode = "JPY").blockingGet()
        assertThat(remoteExchangeRates.success).isFalse()
    }
}