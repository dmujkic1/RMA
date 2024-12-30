package com.example.spirala1

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIAdapter {
    val retrofit : TrefleApiService = Retrofit.Builder()
        .baseUrl("http://trefle.io/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TrefleApiService::class.java)
}