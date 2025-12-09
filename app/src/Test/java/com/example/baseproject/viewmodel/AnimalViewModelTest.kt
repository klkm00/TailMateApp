package com.example.baseproject.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.baseproject.data.AnimalRepository
import com.example.baseproject.model.Animal
import com.example.baseproject.model.AnimalListResponse
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException


@OptIn(ExperimentalCoroutinesApi :: class)
class AnimalViewModelTest {
    // esto basicamente es lo mismo que login viewmodel pero creamos unos datos de prueba para simular
    // con mock y ademas simulamos las calls de la API
    @get: Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var mockRepository: AnimalRepository

    private lateinit var viewModel: AnimalViewModel


    // Estos son datos de prueba para simular
    private val mockAnimalPerro = Animal(
        1,
        "Max",
        "Perro",
        null,
        "1 año",
        "adopcion",
        "Macho",
        null,
        null,
        null,
        1,
        1,
        "url_max",
        null,
        "Metropolitana",
        "Santiago",
        "url_max_detalle"
    )
    private val mockAnimalGato = Animal(
        2,
        "Luna",
        "Gato",
        null,
        "2 años",
        "encontrado",
        "Hembra",
        null,
        null,
        null,
        0,
        1,
        "url_luna",
        null,
        "Valparaíso",
        "Viña del Mar",
        "url_luna_detalle"
    )
    private val mockAnimalConejo = Animal(
        3,
        "Bugs",
        "Conejo",
        null,
        "6 meses",
        "perdido",
        "Macho",
        null,
        null,
        null,
        1,
        0,
        "url_bugs",
        null,
        "Metropolitana",
        "Maipú",
        "url_bugs_detalle"
    )


    // Esto simula las repsuestas de la API
    private val fullResponse =
        AnimalListResponse(data = listOf(mockAnimalPerro, mockAnimalGato, mockAnimalConejo))

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher) // Esto es para que las corrutinas se ejecuten en el hilo de prueba

        mockRepository = mockk(relaxed = true) // inicia el mock del repo

        viewModel = AnimalViewModel(mockRepository) // inicia el viewmodel

    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cargaListadeAnimale_Exito_ActualizaDatos`() = runTest(testDispatcher) {
        // (lO QUE DEBERIA DAR )
        //GIVEN  : El repo deberia devolver la Lista de animales
        coEvery { mockRepository.getAnimalList() } returns fullResponse

        // WHEN:  carga la lista de animales
        viewModel.cargarListaAnimales()


        // Esto Ejecuta la corrutina
        advanceUntilIdle()


        // simula el estado UI done debe dar la lista cargada
        assertFalse(viewModel.listUiState.isLoading)
        assertNull(viewModel.listUiState.error)
        assertEquals(3, viewModel.listUiState.animales.size)
        assertEquals("Max", viewModel.listUiState.animales.first().nombre)
    }

    @Test
    fun `cargarLista_Fallo_actualiza_con_error`() = runTest(testDispatcher) {

        // GIV -> simula el error de la API
        val errorMessage = "Error de red simulado"

        // W -> carga la lista de animales
        viewModel.cargarListaAnimales()

        advanceUntilIdle() // delay

        // T -> Verifica el estado UI y deberia dar error
        assertFalse(viewModel.listUiState.isLoading)
        assertNotNull(viewModel.listUiState.error)
        assertTrue(viewModel.listUiState.error!!.contains(errorMessage))
        assertTrue(viewModel.listUiState.animales.isEmpty())


    }


    // Aqui se van a poner a prueba los filtrados
    @Test
    fun `filtraPorTipo_Exito_ActualizaDatos`() = runTest(testDispatcher) {
        // G -> la repo devuelve una filtracion simulada de perro
        val perroResponse = AnimalListResponse(data = listOf(mockAnimalPerro))

        // configura mock para que devuelva solo perro de los datos simulados
        coEvery { mockRepository.getAnimalsByType("perro") } returns perroResponse

        // W -> filtra por perro
        viewModel.filtrarPorTipo("Perro")
        advanceUntilIdle() // el delay

        // T -> se verifica que se llamo el metodo correcto del repo
        coVerify(exactly = 1) { mockRepository.getAnimalsByType("perro") }
        // se verifica el estado UI que sea correcto
        assertEquals(1, viewModel.listUiState.animales.size)
        assertEquals("Perro", viewModel.listUiState.animales.first().tipo)
    }

    @Test
    fun `filtraPorComuna_conNombredeRegion_Exito_ActualizaDatos`() = runTest(testDispatcher) {

       // simula la respuesta de la API por metropolitana
        val regionResponse = AnimalListResponse(data = listOf(mockAnimalPerro))

        // se configura para que el mock devuelva esa respuesta
        coEvery { mockRepository.getAnimalsByRegion("7") } returns regionResponse

        // El usuario ingresa Metropolitana y devuelve una llista
        viewModel.filtrarPorComuna("Metropolitana")
        advanceUntilIdle() // delay

        // se verifica que coinicidaa con la lladmada
        coVerify(exactly = 1) { mockRepository.getAnimalsByRegion("7") }
        coVerify(exactly = 0) { mockRepository.getAnimalsByComuna(any()) }

        // Se verifica que se actualize el estado UI
        assertEquals(1, viewModel.listUiState.animales.size)
        assertEquals("Metropolitana", viewModel.listUiState.animales.first().region)
    }
    @Test
    fun`filtrarPorComuna_conNombre_de_comuna`() = runTest(testDispatcher) {


        val comunaResponse = AnimalListResponse(data = listOf(mockAnimalConejo))

        coEvery { mockRepository.getAnimalsByComuna("Las Condes") } returns comunaResponse

        viewModel.filtrarPorComuna("Las Condes")
        advanceUntilIdle()

        coVerify(exactly = 1) { mockRepository.getAnimalsByComuna("Las Condes") }
        coVerify(exactly = 0) { mockRepository.getAnimalsByRegion(any()) }

        assertEquals("Maipú", viewModel.listUiState.animales.first().comuna)
    }
}