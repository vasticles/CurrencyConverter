package com.paypay.currencyconverter.ui.main

import android.icu.util.CurrencyAmount
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.paypay.currencyconverter.ui.bindings.bindable

class ExchangeRateViewModel(
    currencyAmount: CurrencyAmount
) : BaseObservable() {

    @get:Bindable
    var amount: Double by bindable(currencyAmount.number.toDouble(), BR.amount)

    @get:Bindable
    var currency: String by bindable(currencyAmount.currency.displayName, BR.currency)

    @get:Bindable
    var symbol: String by bindable(currencyAmount.currency.symbol, BR.symbol)
}