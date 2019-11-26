package com.paypay.currencyconverter.domain.usecases

import android.icu.util.CurrencyAmount
import com.paypay.currencyconverter.domain.datasources.ExchangeRatesDataSource
import com.paypay.currencyconverter.domain.extensions.convert
import io.reactivex.Observable

class RetrieveRatesUseCaseRequest(val currencyAmount: CurrencyAmount)

class RetrieveRatesUseCase(private val exchangeRatesRepo: ExchangeRatesDataSource) :
    UseCase<RetrieveRatesUseCaseRequest, Observable<List<CurrencyAmount>>> {

    override fun execute(request: RetrieveRatesUseCaseRequest): Observable<List<CurrencyAmount>> {

        return exchangeRatesRepo.observe(sourceCurrency = request.currencyAmount.currency)
            .map { rates -> request.currencyAmount.convert(rates) }
    }
}