package com.paypay.currencyconverter.domain.usecases

import android.icu.util.Currency
import android.icu.util.CurrencyAmount
import android.os.Build
import com.paypay.currencyconverter.domain.*
import com.paypay.currencyconverter.domain.datasources.ExchangeRatesDataSource
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class RetrieveRatesUseCaseTest {

    private val dataSource = mockk<ExchangeRatesDataSource>(relaxed = true)
    private val useCase = RetrieveRatesUseCase(dataSource)
    private val rates = mockRates

    @Before
    fun setUp() {
        every { dataSource.observe(any()) } returns Observable.just(rates)
    }

    @Test
    fun `When executing convert currency use case, then the exchange rate data source observe is called with the specified source currency`() {
        val currencyAmount = CurrencyAmount(1000.0, Currency.getInstance("JPY"))
        val request = RetrieveRatesUseCaseRequest(currencyAmount)

        useCase.execute(request).test()

        verify { dataSource.observe(sourceCurrency = request.currencyAmount.currency) }
    }

    @Test
    fun `When converting from USD as the source currency, then the result matches expected`() {
        val amount = 1000.0
        val request = RetrieveRatesUseCaseRequest(CurrencyAmount(amount, usd))
        val expected = listOf(
            CurrencyAmount(amount * usdJpyRate, jpy),
            CurrencyAmount(amount * usdCadRate, cad),
            CurrencyAmount(amount * usdRubRate, rub),
            CurrencyAmount(amount, usd)
        )

        val actual = useCase.execute(request).test().values().first()
        assertThat(actual).containsAll(expected)
    }

    @Test
    fun `When converting from JPY as the source currency, then the result matches expected`() {
        val amount = 1000.0
        val request = RetrieveRatesUseCaseRequest(CurrencyAmount(amount, jpy))
        val expected = listOf(
            CurrencyAmount(amount, jpy),
            CurrencyAmount(amount / usdJpyRate * usdCadRate, cad),
            CurrencyAmount(amount / usdJpyRate * usdRubRate, rub),
            CurrencyAmount(amount / usdJpyRate, usd)
        )

        val actual = useCase.execute(request).test().values().first()
        assertThat(actual).containsAll(expected)
    }

    @Test
    fun `When converting from CAD as the source currency, then the result matches expected`() {
        val amount = 1000.0
        val request = RetrieveRatesUseCaseRequest(CurrencyAmount(amount, cad))
        val expected = listOf(
            CurrencyAmount(amount, cad),
            CurrencyAmount(amount / usdCadRate * usdJpyRate, jpy),
            CurrencyAmount(amount / usdCadRate * usdRubRate, rub),
            CurrencyAmount(amount / usdCadRate, usd)
        )

        val actual = useCase.execute(request).test().values().first()
        assertThat(actual).containsAll(expected)
    }

    @Test
    fun `When converting from RUB as the source currency, then the result matches expected`() {
        val amount = 1000.0
        val request = RetrieveRatesUseCaseRequest(CurrencyAmount(amount, rub))
        val expected = listOf(
            CurrencyAmount(amount, rub),
            CurrencyAmount(amount / usdRubRate * usdCadRate, cad),
            CurrencyAmount(amount / usdRubRate * usdJpyRate, jpy),
            CurrencyAmount(amount / usdRubRate, usd)
        )

        val actual = useCase.execute(request).test().values().first()
        assertThat(actual).containsAll(expected)
    }
}
