package com.example.uas

import com.example.uas.api.ExpenseApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object RetrofitClient {
    private const val BASE_URL = "http://128.199.223.108"
//    private const val BASE_URL = "http://192.168.1.42:3001"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val expenseApiService: ExpenseApiService by lazy {
        retrofit.create(ExpenseApiService::class.java)
    }
}