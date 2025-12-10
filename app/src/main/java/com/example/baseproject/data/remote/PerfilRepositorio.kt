package com.example.baseproject.data.remote
import android.net.Uri
import com.example.baseproject.model.PerfilDeUsuario

class PerfilRepositorio {

    // Perfil actual en memoria
    private var perfilActual = PerfilDeUsuario(
        id = 1,
        nombre = "Usuario",
        imagenUri = null
    )

    // Devuelve el perfil actual
    fun getProfile(): PerfilDeUsuario = perfilActual

    // Actualiza solo la imagen, manteniendo id y nombre
    fun updateImage(uri: Uri?) {
        perfilActual = perfilActual.copy(imagenUri = uri)
    }
}
