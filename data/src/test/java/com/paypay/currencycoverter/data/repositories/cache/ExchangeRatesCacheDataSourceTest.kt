package com.paypay.currencycoverter.data.repositories.cache

import android.os.Build
import com.paypay.currencycoverter.data.repositories.jpy
import com.paypay.currencycoverter.data.repositories.mockRates
import com.paypay.currencycoverter.data.repositories.usd
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
internal class ExchangeRatesRemoteDataSourceTest {

    private val cacheModule = mockk<CacheModule>(relaxed = true)
    private val dataSource = ExchangeRatesCacheDataSource(cacheModule)
    private val rates = mockRates

    @Test
    fun `When exchange rates are saved, Then they get saved by the cache module`() {
        dataSource.save(rates)

        verify { cacheModule.save(mockRates) }
    }

    @Test
    fun `When exchange rates are retrieved, Then they are retrieved by the cache module`() {
        dataSource.retrieve(jpy)

        verify { cacheModule.retrieveExchangeRates(jpy.currencyCode) }
    }

    @Test
    fun `When exchange rates are retrieved with no specified source currency, Then they are retrieved with the default source currency`() {
        dataSource.retrieve()

        verify { cacheModule.retrieveExchangeRates(usd.currencyCode) }
    }
}