package com.example.baseproject.viewmodel
import com.example.baseproject.data.remote.PerfilRepositorio
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class PerfilViewModel(
    private val repositorio: PerfilRepositorio = PerfilRepositorio()
) : ViewModel() {

    // Flow que expone la URI de la imagen
    private val _imagenUri = MutableStateFlow<Uri?>(repositorio.getProfile().imagenUri)
    val imagenUri: StateFlow<Uri?> = _imagenUri

    // Función para actualizar la imagen
    fun setImage(uri: Uri?) {
        viewModelScope.launch {
            _imagenUri.value = uri
            repositorio.updateImage(uri)
        }
    }
}
