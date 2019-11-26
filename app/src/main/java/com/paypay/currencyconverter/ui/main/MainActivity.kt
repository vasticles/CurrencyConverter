package com.paypay.currencyconverter.ui.main

import android.icu.util.Currency
import android.icu.util.CurrencyAmount
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paypay.currencyconverter.MainApplication
import com.paypay.currencyconverter.R
import com.paypay.currencyconverter.databinding.ActivityMainBinding
import com.paypay.currencyconverter.domain.DEFAULT_SOURCE_CURRENCY
import com.paypay.currencyconverter.extensions.keyboard
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class MainActivity : AppCompatActivity(), MainViewModel.Navigator {

    private lateinit var binding: ActivityMainBinding
    private val exchangeRatesRepo = MainApplication.dataComponent.exchangeRatesRepo()
    private val viewModel = MainViewModel(this, exchangeRatesRepo)
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.vm = viewModel

        with(binding.rvRates) {
            adapter = ExchangeRatesAdapter()
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
        binding.spinnerCurrencies.adapter = CurrenciesAdapter(this)

        // populate default
        convert(amount = 0.0, currency = Currency.getInstance(DEFAULT_SOURCE_CURRENCY))
    }

    override fun convert(amount: Double, currency: Currency) {
        keyboard().hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        viewModel.retrieveRates(amount, currency)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {  // extremely basic error handling (which assumes there is no connection)
                Timber.e(it)
                AlertDialog.Builder(this)
                    .setTitle("Couldn't retrieve data")
                    .setMessage("Check your connection and try restarting the app")
                    .create()
                    .show()
            }
            .subscribe {
                Timber.d("Subscribed to exchange rates")
                binding.spinnerCurrencies.setSelection(it.indexOf(CurrencyAmount(amount, currency)))
            }
            .addTo(disposables)
    }

    override fun onPause() {
        super.onPause()
        disposables.clear()
    }
}
