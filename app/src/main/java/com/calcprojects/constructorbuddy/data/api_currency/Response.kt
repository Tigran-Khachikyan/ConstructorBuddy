package com.calcprojects.constructorbuddy.data.api_currency

data class Response(
    val base: String,
    val date: String,
    val rates: Rates
)