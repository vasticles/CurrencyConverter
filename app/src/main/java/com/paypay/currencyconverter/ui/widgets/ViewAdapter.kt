package com.paypay.currencyconverter.ui.widgets

import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.paypay.currencyconverter.extensions.inflater

abstract class ViewAdapter<T> : RecyclerView.Adapter<ViewHolder<T>>() {

    abstract var items: MutableList<T>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        val inflater = parent.context.inflater()
        val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, viewType, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        val binding = items[position]
        holder.bind(binding)
    }

    override fun getItemViewType(position: Int): Int = getLayoutId(position)

    protected abstract fun getLayoutId(position: Int): Int

    override fun getItemCount(): Int = items.size
}