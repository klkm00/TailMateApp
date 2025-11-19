package com.example.baseproject.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baseproject.data.AnimalRepository
import com.example.baseproject.model.Animal
import kotlinx.coroutines.launch

data class AnimalListUiState(
    val animales: List<Animal> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class AnimalDetailUiState(
    val animal: Animal? = null,
    val isLoading: Boolean = false,
    val error: String? = null
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

        // Usamos copy para mantener el estado previo mientras carga
        listUiState = listUiState.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val response = repository.getAnimalList()
                listUiState = listUiState.copy(
                    isLoading = false,
                    animales = response.data,
                    error = null
                )
            } catch (e: Exception) {
                listUiState = listUiState.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun limpiarFiltros() {
        listUiState = listUiState.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val response = repository.getAnimalList()
                listUiState = listUiState.copy(
                    isLoading = false,
                    animales = response.data,
                    error = null
                )
            } catch (e: Exception) {
                listUiState = listUiState.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun filtrarPorTipo(tipo: String) {
        listUiState = listUiState.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val response = repository.getAnimalsByType(tipo.lowercase())
                listUiState = listUiState.copy(
                    isLoading = false,
                    animales = response.data,
                    error = null
                )
            } catch (e: Exception) {
                listUiState = listUiState.copy(
                    isLoading = false,
                    error = "No se encontraron animales de tipo $tipo"
                )
            }
        }
    }

    fun filtrarPorRegion(region: String) {
        listUiState = listUiState.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val response = repository.getAnimalsByRegion(region.trim())
                listUiState = listUiState.copy(
                    isLoading = false,
                    animales = response.data,
                    error = null
                )
            } catch (e: Exception) {
                listUiState = listUiState.copy(
                    isLoading = false,
                    error = "Error al filtrar región: ${e.message}"
                )
            }
        }
    }

    // Búsqueda Inteligente: Usa el mapa para detectar regiones por nombre
    fun filtrarPorComuna(termino: String) {
        listUiState = listUiState.copy(isLoading = true, error = null)

        // Convertimos a minúsculas y quitamos espacios
        val terminoLimpio = termino.lowercase().trim()

        // Revisamos si lo que escribiste coincide con alguna Región del mapa
        val regionId = regionesMap[terminoLimpio]

        viewModelScope.launch {
            try {
                val response = if (regionId != null) {
                    // ¡Es una región! Usamos el ID (ej: "santiago" -> "7")
                    repository.getAnimalsByRegion(regionId)
                } else {
                    // Si no es región, buscamos por Comuna tal cual
                    repository.getAnimalsByComuna(termino.trim())
                }
                
                listUiState = listUiState.copy(
                    isLoading = false,
                    animales = response.data,
                    error = null
                )
            } catch (e: Exception) {
                listUiState = listUiState.copy(
                    isLoading = false,
                    error = "No se encontraron resultados para: $termino"
                )
            }
        }
    }

    fun filtrarPorEstado(estado: String) {
        listUiState = listUiState.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val response = repository.getAnimalsByEstado(estado.lowercase())
                listUiState = listUiState.copy(
                    isLoading = false,
                    animales = response.data,
                    error = null
                )
            } catch (e: Exception) {
                listUiState = listUiState.copy(
                    isLoading = false,
                    error = "No se encontraron animales con estado '$estado'. (${e.message})"
                )
            }
        }
    }

    var detailUiState by mutableStateOf(AnimalDetailUiState())
        private set

    fun cargaDetalleAnimal(id: Int) {
        detailUiState = detailUiState.copy(isLoading = true, error = null)

        viewModelScope.launch {
            // Intentamos buscar primero en la lista local
            val animal = listUiState.animales.find { it.id == id }

            if (animal != null) {
                detailUiState = detailUiState.copy(
                    isLoading = false,
                    animal = animal,
                    error = null
                )
            } else {
                // Si no está en local, recargamos la lista (o podríamos llamar al endpoint de detalle si existiera)
                try {
                    val response = repository.getAnimalList()
                    // Opcional: actualizamos también la lista principal
                    // listUiState = listUiState.copy(animales = response.data)
                    
                    val animalNuevo = response.data.find { it.id == id }
                    if (animalNuevo != null) {
                        detailUiState = detailUiState.copy(
                            isLoading = false,
                            animal = animalNuevo,
                            error = null
                        )
                    } else {
                        detailUiState = detailUiState.copy(
                            isLoading = false,
                            error = "Animal no encontrado"
                        )
                    }
                } catch (e: Exception) {
                    detailUiState = detailUiState.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }
}
