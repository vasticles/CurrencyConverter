package com.paypay.currencyconverter.domain.usecases

interface UseCase<Request, Response> {
    fun execute(request: Request): Response
}