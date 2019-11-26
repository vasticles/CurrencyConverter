package com.paypay.currencycoverter.data.repositories

import android.os.Build
import com.paypay.currencyconverter.domain.datasources.ExchangeRatesDataSource
import io.mockk.*
import io.reactivex.Observable
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
internal class ExchangeRatesRepoTest {

    private val memory = mockk<ExchangeRatesDataSource>(relaxed = true, name = "memory")
    private val local = mockk<ExchangeRatesDataSource>(relaxed = true, name = "local")
    private val remote = mockk<ExchangeRatesDataSource>(relaxed = true, name = "remote")
    private val repo =
        spyk(objToCopy = ExchangeRatesRepo(memory, local, remote), recordPrivateCalls = true, name = "repo")

    private val entity = mockRates

    @Test
    fun `When save is called, Then exchange rates is saved to memory and local data sources`() {
        repo.save(entity)

        verify { memory.save(entity) }
        verify { local.save(entity) }
    }

    @Test
    fun `When retrieving exchange rates, Then rates are retrieved from memory`() {
        every { memory.retrieve() } returns entity

        repo.retrieve()

        verify { memory.retrieve() }
    }

    @Test
    fun `When retrieving exchange rates and they are not available in memory, Then rates are retrieved from local`() {
        every { memory.retrieve() } returns null
        every { local.retrieve() } returns entity

        repo.retrieve()

        verify(ordering = Ordering.ORDERED) {
            memory.retrieve()
            local.retrieve()
        }
    }

    @Test
    fun `When exchange rates are retrieved from local, Then they are saved to memory`() {
        every { memory.retrieve() } returns null
        every { local.retrieve() } returns entity

        repo.retrieve()

        verify(ordering = Ordering.ORDERED) {
            local.retrieve()
            memory.save(entity)
        }
    }

    @Test
    fun `When observing exchange rates, Then rates are retrieved from memory, and remote data source is not observed`() {
        every { memory.retrieve() } returns entity

        repo.observe().test()

        verify { memory.retrieve() }
        verify(exactly = 0) { remote.observe() }
    }

    @Test
    fun `When observing exchange rates and they are not available in memory, Then rates are retrieved from local, and remote data source is not observed`() {
        every { local.retrieve(any()) } returns entity
        repo.observe().test()

        verify {
            memory.retrieve()
            local.retrieve()
        }
        verify(exactly = 0) { remote.observe() }
    }

    @Test
    fun `When observing exchange rates and they are not available in memory or local, Then rates are fetched from remote`() {
        every { remote.observe() } returns Observable.just(entity)

        repo.observe().test()

        verify(ordering = Ordering.ORDERED) {
            repo.retrieve()
            remote.observe()
            repo.save(entity)
        }
    }

    @Test
    fun `When exchange rates are fetched from remote, Then they are saved`() {
        every { remote.observe() } returns Observable.just(entity)

        repo.observe().test()

        verify(ordering = Ordering.ORDERED) {
            remote.observe()
            repo.save(entity)
        }
    }
}