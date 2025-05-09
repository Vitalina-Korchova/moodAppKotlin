package com.example.moodapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://run.mocky.io/") // базова частина URL
            .addConverterFactory(GsonConverterFactory.create()) //буде автоматично конвертовано з джсон
            .build()
            .create(ApiService::class.java)
    }
}