package com.example.baseproject.model

import com.google.gson.annotations.SerializedName

data class Animal(
    val id: Int,
    val nombre: String,
    val tipo: String,
    val color: String?,
    val edad: String?,
    val estado: String,
    val genero: String?,
    @SerializedName("desc_fisica")
    val descripcionFisica: String?,
    @SerializedName("desc_personalidad")
    val descripcionPersonalidad: String?,
    @SerializedName("desc_adicional")
    val descripcionAdicional: String?,
    val esterilizado: Int?,
    val vacunas: Int?,
    val imagen: String,
    val equipo: String?,
    val region: String?,
    val comuna: String?,
    val url: String
)

data class AnimalListResponse(
    val data: List<Animal>
)

data class AnimalImage(
    @SerializedName("imagen")
    val imagenUrl: String
)
