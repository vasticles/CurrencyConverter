package com.paypay.currencyconverter.ui.widgets

abstract class SingleLayoutAdapter<T>(private val layoutId: Int) : ViewAdapter<T>() {
    override fun getLayoutId(position: Int): Int = layoutId
}