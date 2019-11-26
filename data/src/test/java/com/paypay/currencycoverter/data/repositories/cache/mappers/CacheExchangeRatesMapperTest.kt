package com.paypay.currencycoverter.data.repositories.cache.mappers

import android.os.Build
import com.paypay.currencycoverter.data.repositories.*
import com.paypay.currencycoverter.data.repositories.cache.models.CacheExchangeRate
import com.paypay.currencycoverter.data.repositories.cache.models.CacheExchangeRates
import io.realm.RealmList
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
internal class CacheExchangeRatesMapperTest {

    private val mapper = CacheExchangeRatesMapper()

    private val entity = mockRates

    private val cacheRates = RealmList(
        CacheExchangeRate("USDJPY", usdJpyRate),
        CacheExchangeRate("USDCAD", usdCadRate),
        CacheExchangeRate("USDRUB", usdRubRate),
        CacheExchangeRate("USDUSD", usdUsdRate)
    )

    private val cache = CacheExchangeRates(
        sourceCurrency = "USD",
        retrievedAt = retrievedAt.toEpochSecond(),
        rates = cacheRates
    )

    @Test
    fun `When the entity is mapped to a remote model, Then the resulting model matches the expected`() {
        val expected = cache
        val actual = mapper.map(entity)

        assertThat(actual).isEqualToIgnoringGivenFields(expected, "rates")

        expected.rates.forEachIndexed { i, expectedRate ->
            assertThat(actual.rates[i]).isEqualToComparingFieldByField(expectedRate)
        }
    }

    @Test
    fun `When the cache model is mapped to an entity, Then the resulting entity matches the expected`() {
        val expected = entity
        val actual = mapper.reverseMap(cache)
        assertThat(actual).isEqualTo(expected)
    }
}