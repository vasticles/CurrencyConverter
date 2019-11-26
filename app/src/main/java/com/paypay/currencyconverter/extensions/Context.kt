package com.paypay.currencyconverter.extensions

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager

fun Context.inflater(): LayoutInflater = LayoutInflater.from(this)
fun Activity.keyboard(): InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
