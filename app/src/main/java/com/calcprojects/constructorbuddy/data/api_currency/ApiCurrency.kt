package com.calcprojects.constructorbuddy.data.api_currency

import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface ApiCurrency {

    @GET("latest")
    fun getLatestRatesAsync(): Deferred<ResponseCurMetal>
}


