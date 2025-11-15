package com.example.baseproject.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    private const val BASE_URL = "https://huachitos.cl/"

    //Retrofit se crea de forma lazy para no inicializar la red hasta que realmente se necesite.
    val api: AnimalApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AnimalApiService::class.java)
    }
}
