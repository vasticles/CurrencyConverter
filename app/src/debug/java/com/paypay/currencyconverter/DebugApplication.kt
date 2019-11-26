package com.paypay.currencyconverter

import timber.log.Timber

class DebugApplication : MainApplication() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(LinkingDebugTree())
    }
}