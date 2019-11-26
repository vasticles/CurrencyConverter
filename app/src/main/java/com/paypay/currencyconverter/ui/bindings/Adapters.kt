package com.paypay.currencyconverter.ui.bindings

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.paypay.currencyconverter.ui.widgets.ViewAdapter
import timber.log.Timber

object Adapters {

    @JvmStatic
    @BindingAdapter("items")
    fun <T> setItems(view: RecyclerView, items: MutableList<T>) {
        Timber.d("Setting items: ${items.size}")
        val adapter = view.adapter as ViewAdapter<T>
        adapter.items = items
        adapter.notifyDataSetChanged()
    }
}
