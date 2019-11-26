package com.paypay.currencycoverter.data.di

import com.paypay.currencycoverter.data.repositories.ExchangeRatesRepo
import dagger.Component
import io.realm.Realm
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class])
interface DataComponent {

    @Named("Production")
    fun productionInstance(): Realm

    fun exchangeRatesRepo(): ExchangeRatesRepo
}