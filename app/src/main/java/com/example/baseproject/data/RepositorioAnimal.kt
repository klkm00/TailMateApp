package com.example.baseproject.data

import com.example.baseproject.data.remote.NetworkModule
import com.example.baseproject.data.remote.AnimalApiService
import com.example.baseproject.model.AnimalListResponse


class AnimalRepository(
    private val api: AnimalApiService = NetworkModule.api
) {

    //Trae la lista completa de animales aasas
    suspend fun getAnimalList(): AnimalListResponse {
        return api.fetchAnimalList()
    }

    // Filtra por tipo
    suspend fun getAnimalsByType(tipo: String): AnimalListResponse {
        return api.fetchAnimalsByType(tipo)
    }

    // Filtra por regiónsd
    suspend fun getAnimalsByRegion(region: String): AnimalListResponse {
        return api.fetchAnimalsByRegion(region)
    }

    // Filtra por comuna
    suspend fun getAnimalsByComuna(comuna: String): AnimalListResponse {
        return api.fetchAnimalsByComuna(comuna)
    }

    // Filtra por estado
    suspend fun getAnimalsByEstado(estado: String): AnimalListResponse {
        return api.fetchAnimalsByEstado(estado)
    }
}
