package com.example.baseproject.data

import com.example.baseproject.data.remote.NetworkModule
import com.example.baseproject.data.remote.AnimalApiService
import com.example.baseproject.model.AnimalDetail
import com.example.baseproject.model.AnimalListResponse


class AnimalRepository(
    private val api: AnimalApiService = NetworkModule.api
) {

    //Trae la lista de animales de la api
    suspend fun getAnimalList(): AnimalListResponse {
        return api.fetchAnimalList()
    }

    //Trae los detalles de cada animal
    suspend fun getAnimalDetail(id: Int): AnimalDetail {
        return api.fetchAnimalDetail(id)
    }

}
