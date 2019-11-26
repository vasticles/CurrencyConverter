package com.paypay.currencyconverter.ui.main

import com.paypay.currencyconverter.R
import com.paypay.currencyconverter.ui.widgets.SingleLayoutAdapter

class ExchangeRatesAdapter(override var items: MutableList<ExchangeRateViewModel> = mutableListOf()) :
    SingleLayoutAdapter<ExchangeRateViewModel>(R.layout.item_exchange_rate)