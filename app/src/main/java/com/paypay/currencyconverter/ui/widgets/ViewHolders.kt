package com.paypay.currencyconverter.ui.widgets

import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView

class ViewHolder<T>(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(vm: T) {
        binding.setVariable(BR.vm, vm)
        binding.executePendingBindings()
    }
}