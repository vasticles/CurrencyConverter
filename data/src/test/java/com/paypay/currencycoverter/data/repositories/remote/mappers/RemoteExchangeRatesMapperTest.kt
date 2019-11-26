package com.paypay.currencycoverter.data.repositories.remote.mappers

import android.os.Build
import com.paypay.currencycoverter.data.repositories.*
import com.paypay.currencycoverter.data.repositories.remote.models.RemoteExchangeRates
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
internal class RemoteExchangeRatesMapperTest {

    private val entity = mockRates

    private val remote = RemoteExchangeRates(
        success = true,
        source = usd.currencyCode,
        retrievedAt = retrievedAt,
        quotes = mapOf(
            usd.currencyCode + jpy.currencyCode to usdJpyRate,
            usd.currencyCode + cad.currencyCode to usdCadRate,
            usd.currencyCode + rub.currencyCode to usdRubRate,
            usd.currencyCode + usd.currencyCode to usdUsdRate
        )
    )

    @Test
    fun reverseMap() {
        val mapper = RemoteExchangeRatesMapper()
        val expected = entity
        val actual = mapper.reverseMap(remote)
        assertThat(actual).isEqualTo(expected)
    }
}