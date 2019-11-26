package com.paypay.currencycoverter.data.repositories.remote

import android.os.Build
import com.paypay.currencycoverter.data.repositories.jpy
import com.paypay.currencycoverter.data.repositories.remote.models.RemoteExchangeRates
import com.paypay.currencycoverter.data.repositories.usd
import io.mockk.Ordering
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
internal class ExchangeRatesRemoteDataSourceTest {

    private val api = mockk<CurrencyConverterApi>(relaxed = true)
    private val networkModule = mockk<NetworkModule>(relaxed = true) {
        every { currencyConverterApi } returns api
    }
    private val dataSource = ExchangeRatesRemoteDataSource(networkModule)
    private val validCurrency = usd
    private val invalidCurrency = jpy

    @Test
    fun `When exchange rates with a valid source currency are observed, Then the remote api is accessed once`() {
        every { api.fetchRates(validCurrency.currencyCode) } returns Single.just(RemoteExchangeRates(success = true))

        dataSource.observe(validCurrency).test()

        verify(exactly = 1) { api.fetchRates(any()) }
    }

    @Test
    fun `When exchange rates with an invalid source currency are observed, Then the remote api is accessed twice`() {
        every { api.fetchRates(invalidCurrency.currencyCode) } returns Single.just(RemoteExchangeRates(success = false))

        dataSource.observe(invalidCurrency).test()

        verify(exactly = 2) { api.fetchRates(any()) }
    }

    @Test
    fun `When exchange rates with an invalid source currency are observed, Then the remote api request falls back to default currency`() {
        every { api.fetchRates(invalidCurrency.currencyCode) } returns Single.just(RemoteExchangeRates(success = false))

        dataSource.observe(invalidCurrency).test()

        verify(ordering = Ordering.ORDERED) {
            api.fetchRates(invalidCurrency.currencyCode)
            api.fetchRates()
        }
    }
}

