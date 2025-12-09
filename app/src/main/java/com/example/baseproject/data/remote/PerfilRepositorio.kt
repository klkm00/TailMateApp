package com.example.baseproject.data.remote

class PerfilRepositorio {
    private var perfilActual = PerfilDeUsuario(
    id = 1,
    nombre = "Usuario",
    imagenUri = null
    )
}

fun getProfile(): PerfilDeUsuario = perfilActual

fun updateImage(uri: Uri?) {
perfilActual = perfilActual.copy(imagenUri = uri)
}