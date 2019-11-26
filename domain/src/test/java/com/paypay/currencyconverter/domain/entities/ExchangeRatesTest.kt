package com.paypay.currencyconverter.domain.entities

import android.os.Build
import com.paypay.currencyconverter.domain.mockRates
import com.paypay.currencyconverter.domain.now
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
internal class ExchangeRatesTest {

    val rates = mockRates

    @Test
    fun `When exchange rates retrievedAt date is under 30 minutes, and they contain more than 1 currency rate, Then they are valid`() {
        assertThat(rates.areValid).isTrue()
    }

    @Test
    fun `When exchange rates retrievedAt date is over 30 minutes, Then they are not valid`() {
        val newRates = rates.copy(retrievedAt = now.minusMinutes(31))
        assertThat(newRates.areValid).isFalse()
    }

    @Test
    fun `When exchange rates contain 0 currency rates, Then they are not valid`() {
        val newRates = rates.copy(rates = emptyMap())
        assertThat(newRates.areValid).isFalse()
    }
}