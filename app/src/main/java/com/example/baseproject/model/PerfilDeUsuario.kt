package com.example.baseproject.model

data class PerfilDeUsuario(
    val id: Int,
    val nombre: String,
    val imagenUri: Uri? = null
)