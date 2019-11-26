package com.paypay.currencycoverter.data.di

import dagger.Component
import io.realm.Realm
import javax.inject.Named
import javax.inject.Provider

//FIXME: this class has to be in main source set rather than androidTest,
//       because kapt is not picking it up at build time

@Component(modules = [DataModule::class])
interface TestComponent {
    @Named("EmptyTest")
    fun emptyTestInstance(): Provider<Realm>

    @Named("PopulatedTest")
    fun populatedTestInstance(): Provider<Realm>
}