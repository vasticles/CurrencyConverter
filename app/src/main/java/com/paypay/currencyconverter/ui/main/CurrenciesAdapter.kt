package com.paypay.currencyconverter.ui.main

import android.content.Context
import android.icu.util.Currency
import android.widget.ArrayAdapter

class CurrenciesAdapter(context: Context, var items: List<Currency> = emptyList()) :
    ArrayAdapter<Currency>(context, android.R.layout.simple_spinner_item, items) {


}