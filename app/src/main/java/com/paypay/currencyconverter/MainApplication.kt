package com.paypay.currencyconverter

import android.app.Application
import com.paypay.currencycoverter.data.di.DaggerDataComponent
import com.paypay.currencycoverter.data.di.DataComponent
import com.paypay.currencycoverter.data.di.DataModule

open class MainApplication : Application() {

    companion object {
        lateinit var dataComponent: DataComponent
    }

    override fun onCreate() {
        super.onCreate()

        dataComponent = DaggerDataComponent.builder()
            .dataModule(DataModule(this))
            .build()
    }
}