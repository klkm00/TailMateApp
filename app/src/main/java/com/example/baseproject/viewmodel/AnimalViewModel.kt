package com.example.baseproject.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baseproject.data.AnimalRepository
import com.example.baseproject.model.Animal
import kotlinx.coroutines.launch

data class  AnimalListUiState(
    val animales: List<Animal> = emptyList(),
    val isLoading: Boolean = false,
    val error : String? = null
)

data class AnimalDetailUiState(
    val animal: Animal? = null,
    val isLoading: Boolean = false,
    val error : String? = null
)

class AnimalViewModel(
    private val repository: AnimalRepository = AnimalRepository()
) : ViewModel() {

    var listUiState by mutableStateOf(AnimalListUiState())
        private set

    // Mapa manual de regiones y sus IDs
    private val regionesMap = mapOf(
        "arica" to "1", "arica y parinacota" to "1",
        "tarapaca" to "2", "iquique" to "2",
        "antofagasta" to "3",
        "atacama" to "4", "copiapo" to "4",
        "coquimbo" to "5", "la serena" to "5",
        "valparaiso" to "6", "valpo" to "6", "viña" to "6", "vina" to "6",
        "rm" to "7", "metropolitana" to "7", "santiago" to "7",
        "ohiggins" to "8", "rancagua" to "8",
        "maule" to "9", "talca" to "9",
        "nuble" to "10", "chillan" to "10",
        "biobio" to "11", "concepcion" to "11",
        "araucania" to "12", "temuco" to "12",
        "los rios" to "13", "valdivia" to "13",
        "los lagos" to "14", "puerto montt" to "14",
        "aisen" to "15", "coyhaique" to "15",
        "magallanes" to "16", "punta arenas" to "16"
    )

    fun cargarListaAnimales() {
        if (listUiState.animales.isNotEmpty()) return 

        listUiState = AnimalListUiState(isLoading = true)

        viewModelScope.launch {
            try {
                val response = repository.getAnimalList()
                listUiState = AnimalListUiState(animales = response.data)
            } catch (e: Exception) {
                listUiState = AnimalListUiState(error = e.message)
            }
        }
    }

    fun limpiarFiltros() {
        listUiState = AnimalListUiState(isLoading = true)
        viewModelScope.launch {
            try {
                val response = repository.getAnimalList()
                listUiState = AnimalListUiState(animales = response.data)
            } catch (e: Exception) {
                listUiState = AnimalListUiState(error = e.message)
            }
        }
    }

    fun filtrarPorTipo(tipo: String) {
        listUiState = AnimalListUiState(isLoading = true)
        viewModelScope.launch {
            try {
                val response = repository.getAnimalsByType(tipo.lowercase())
                listUiState = AnimalListUiState(animales = response.data)
            } catch (e: Exception) {
                listUiState = AnimalListUiState(error = "No se encontraron animales de tipo $tipo")
            }
        }
    }

    fun filtrarPorRegion(region: String) {
        listUiState = AnimalListUiState(isLoading = true)
        viewModelScope.launch {
            try {
                val response = repository.getAnimalsByRegion(region.trim())
                listUiState = AnimalListUiState(animales = response.data)
            } catch (e: Exception) {
                listUiState = AnimalListUiState(error = "Error al filtrar región: ${e.message}")
            }
        }
    }

    // Búsqueda Inteligente: Usa el mapa para detectar regiones por nombre
    fun filtrarPorComuna(termino: String) {
        listUiState = AnimalListUiState(isLoading = true)
        
        // Convertimos a minúsculas y quitamos espacios
        val terminoLimpio = termino.lowercase().trim()
        
        // Revisamos si lo que escribiste coincide con alguna Región del mapa
        val regionId = regionesMap[terminoLimpio]

        viewModelScope.launch {
            try {
                if (regionId != null) {
                    // ¡Es una región! Usamos el ID (ej: "santiago" -> "7")
                    val response = repository.getAnimalsByRegion(regionId)
                    listUiState = AnimalListUiState(animales = response.data)
                } else {
                    // Si no es región, buscamos por Comuna tal cual
                    val response = repository.getAnimalsByComuna(termino.trim())
                    listUiState = AnimalListUiState(animales = response.data)
                }
            } catch (e: Exception) {
                listUiState = AnimalListUiState(error = "No se encontraron resultados para: $termino")
            }
        }
    }

    fun filtrarPorEstado(estado: String) {
        listUiState = AnimalListUiState(isLoading = true)
        viewModelScope.launch {
            try {
                val response = repository.getAnimalsByEstado(estado.lowercase())
                listUiState = AnimalListUiState(animales = response.data)
            } catch (e: Exception) {
                listUiState = AnimalListUiState(error = "No se encontraron animales con estado '$estado'. (${e.message})")
            }
        }
    }

    var detailUiState by mutableStateOf(AnimalDetailUiState())
       private set

    fun cargaDetalleAnimal(id: Int) {
        detailUiState = AnimalDetailUiState(isLoading = true)

        viewModelScope.launch {
            val animal = listUiState.animales.find { it.id == id }
            
            if (animal != null) {
                detailUiState = AnimalDetailUiState(animal = animal)
            } else {
                try {
                    val response = repository.getAnimalList()
                    listUiState = AnimalListUiState(animales = response.data)
                    val animalNuevo = response.data.find { it.id == id }
                    if (animalNuevo != null) {
                        detailUiState = AnimalDetailUiState(animal = animalNuevo)
                    } else {
                        detailUiState = AnimalDetailUiState(error = "Animal no encontrado")
                    }
                } catch (e: Exception) {
                    detailUiState = AnimalDetailUiState(error = e.message)
                }
            }
        }
    }
}
