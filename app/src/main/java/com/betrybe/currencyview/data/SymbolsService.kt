package com.betrybe.currencyview.data

import com.betrybe.currencyview.data.api.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SymbolsService {

    private const val API_KEY = "PfkOx6cTtpf7W7HHqxcRkCn7mlY9YK77"

    private const val BASE_URL = "https://api.apilayer.com/exchangerates_data/"

    val instance: ApiService by lazy {

        val apiKeyInterceptor = ApiKeyInterceptor(API_KEY)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        retrofit.create(ApiService::class.java)
    }
}
