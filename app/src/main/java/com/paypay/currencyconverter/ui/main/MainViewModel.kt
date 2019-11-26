package com.paypay.currencyconverter.ui.main

import android.icu.util.Currency
import android.icu.util.CurrencyAmount
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.paypay.currencyconverter.domain.datasources.ExchangeRatesDataSource
import com.paypay.currencyconverter.domain.usecases.RetrieveRatesUseCase
import com.paypay.currencyconverter.domain.usecases.RetrieveRatesUseCaseRequest
import com.paypay.currencyconverter.ui.bindings.bindable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber

class MainViewModel(
    private val navigator: Navigator,
    exchangeRatesRepo: ExchangeRatesDataSource,
    private val retrieveRatesUseCase: RetrieveRatesUseCase = RetrieveRatesUseCase(exchangeRatesRepo)
) : BaseObservable() {

    interface Navigator {
        fun convert(amount: Double, currency: Currency)
    }

    @get:Bindable
    var amount: String by bindable("", BR.amount)

    @get:Bindable
    var selectedCurrencyIndex: Int by bindable(0, BR.selectedCurrencyIndex)

    @get:Bindable
    var currencies: List<Currency> by bindable(emptyList(), BR.currencies)

    @get:Bindable
    var rates: List<ExchangeRateViewModel> by bindable(emptyList(), BR.rates)

    fun convert() {
        navigator.convert(amount.toDouble(), currencies[selectedCurrencyIndex])
    }

    fun retrieveRates(amount: Double = 1.0, currency: Currency): Observable<List<CurrencyAmount>> {
        Timber.d("Converting $amount ${currency.currencyCode}")
        val currencyAmount = CurrencyAmount(amount, currency)
        return retrieveRatesUseCase.execute(RetrieveRatesUseCaseRequest(currencyAmount))
            .observeOn(AndroidSchedulers.mainThread())
            .map { retrievedRates ->
                rates = retrievedRates.map { currencyAmount ->
                    ExchangeRateViewModel(currencyAmount)
                }
                currencies = retrievedRates.map { currencyAmount ->
                    currencyAmount.currency
                }
                retrievedRates
            }
    }
}