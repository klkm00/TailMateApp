package com.example.baseproject.data.remote

import com.example.baseproject.model.AnimalListResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface AnimalApiService {

    @GET("api/animales")
    suspend fun fetchAnimalList(): AnimalListResponse

    @GET("api/animales/tipo/{tipo}")
    suspend fun fetchAnimalsByType(@Path("tipo") tipo: String): AnimalListResponse

    @GET("api/animales/region/{region}")
    suspend fun fetchAnimalsByRegion(@Path("region") region: String): AnimalListResponse

    @GET("api/animales/comuna/{comuna}")
    suspend fun fetchAnimalsByComuna(@Path("comuna") comuna: String): AnimalListResponse

    @GET("api/animales/estado/{estado}")
    suspend fun fetchAnimalsByEstado(@Path("estado") estado: String): AnimalListResponse
}
