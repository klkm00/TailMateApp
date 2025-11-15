package com.example.baseproject.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baseproject.data.AnimalRepository
import com.example.baseproject.model.Animal
import com.example.baseproject.model.AnimalDetail // hay que hacer los detalles de animal
import kotlinx.coroutines.launch

data class  AnimalListUiState(
    val animales: List<Animal> = emptyList(),
    val isLoading: Boolean = false,
    val error : String? = null
)

data class AnimalDetailUiState(
    val animal: AnimalDetail? = null,
    val isLoading: Boolean = false,
    val error : String? = null
)

class AnimalViewModel(
    private val repository: AnimalRepository = AnimalRepository() // Esto depende del repositorio
) : ViewModel() {

    var listUiState by mutableStateOf(AnimalListUiState())
    fun cargarListaAnimales() {
        listUiState = AnimalListUiState(isLoading = true)

        viewModelScope.launch {
            try {
                val response = repository.getAnimalList()
                listUiState = AnimalListUiState(animales = response.data)
            } catch (e: Exception) {
            }

        }

    }

    var detailUiState by mutableStateOf(AnimalDetailUiState())
       private set
    fun cargaDetalleAnimal(id: Int) {
        detailUiState = AnimalDetailUiState(isLoading = true)

        viewModelScope.launch {
            try{
                val animal = repository.getAnimalDetail(id)
                detailUiState = AnimalDetailUiState(animal = animal)
            } catch (e: Exception) {
                detailUiState = AnimalDetailUiState(error = e.message)
            }
        }
    }
}