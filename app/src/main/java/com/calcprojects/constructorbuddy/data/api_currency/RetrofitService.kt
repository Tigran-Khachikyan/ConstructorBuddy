package com.calcprojects.constructorbuddy.data.api_currency

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//https://api.currencyscoop.com/v1/historical?api_key=0bba3f2c5f3083ba3623938dbf492052&date=2020-01-12
//https://api.currencyscoop.com/v1/latest?api_key=0bba3f2c5f3083ba3623938dbf492052
//Base USD

private const val BASE_URL = "https://api.currencyscoop.com/v1/"
private const val API_KEY = "0bba3f2c5f3083ba3623938dbf492052"
private const val HEADER = "api_key"

object RetrofitService {

    private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val author = Interceptor { chain ->
        val url = chain.request()
            .url()
            .newBuilder()
            .addQueryParameter(HEADER, API_KEY)
            .build()
        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()
        chain.proceed(request)
    }
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(author)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}