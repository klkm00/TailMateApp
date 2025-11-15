package com.example.baseproject.data.remote

import com.example.baseproject.model.AnimalDetail
import com.example.baseproject.model.AnimalListResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface AnimalApiService {

    @GET("api/animales")
    suspend fun fetchAnimalList(): AnimalListResponse

    @GET("api/animales/{id}")
    suspend fun fetchAnimalDetail(@Path("id") id: Int): AnimalDetail

}
