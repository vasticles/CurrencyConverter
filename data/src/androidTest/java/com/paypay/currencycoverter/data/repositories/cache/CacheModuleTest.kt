package com.paypay.currencycoverter.data.repositories.cache

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.paypay.currencycoverter.data.di.DaggerTestComponent
import com.paypay.currencycoverter.data.di.DataModule
import com.paypay.currencycoverter.data.di.TestComponent
import com.paypay.currencycoverter.data.mockRates
import com.paypay.currencycoverter.data.repositories.cache.models.CacheExchangeRates
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class CacheModuleTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val testComponent: TestComponent = DaggerTestComponent.builder()
        .dataModule(DataModule(context))
        .build()

    private val entity = mockRates

    @Test
    fun save() {
        val realm = testComponent.emptyTestInstance().get()
        CacheModule.init(realm)

        CacheModule.save(entity)
        val result = realm.where(CacheExchangeRates::class.java).findFirst()

        assertThat(result).isNotNull
        assertThat(result?.retrievedAt).isEqualTo(entity.retrievedAt.toEpochSecond())
        assertThat(result?.rates?.size).isEqualTo(entity.rates.size)
    }

    @Test
    fun retrieveExchangeRates() {
        val realm = testComponent.populatedTestInstance().get()
        CacheModule.init(realm)

        val result = CacheModule.retrieveExchangeRates("USD")

        assertThat(result).isNotNull
        assertThat(result?.retrievedAt).isEqualTo(entity.retrievedAt)
        assertThat(result?.rates).hasSameSizeAs(entity.rates)
    }
}