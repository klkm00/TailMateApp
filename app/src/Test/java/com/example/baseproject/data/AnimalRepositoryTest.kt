package com.example.baseproject.data


import com.example.baseproject.data.remote.AnimalApiService
import com.example.baseproject.model.AnimalListResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AnimalRepositoryTest {

    private lateinit var mockApiService: AnimalApiService

    private lateinit var repository: AnimalRepository

    private val mockResponse = AnimalListResponse(data = emptyList())

    @Before
    fun setup() {
        mockApiService = mockk()

        repository = AnimalRepository(mockApiService)
    }
    @Test
    fun `getAnimalList_llamaAMetodoCorrectoDeApi_yRetornaRespuesta`() = runTest {

        coEvery { mockApiService.fetchAnimalList() } returns mockResponse

        val result = repository.getAnimalList()

        coVerify(exactly = 1) { mockApiService.fetchAnimalList() }

        assertEquals(mockResponse, result)
    }

    @Test
    fun `getAnimalsByType_llamaAEndpointTipoConParametroCorrecto`() = runTest {
        val filtro = "gato"
        coEvery { mockApiService.fetchAnimalsByType(filtro) } returns mockResponse

        repository.getAnimalsByType(filtro)

        coVerify(exactly = 1) { mockApiService.fetchAnimalsByType(filtro) }
    }

    @Test
    fun `getAnimalsByRegion_llamaAEndpointRegionConParametroCorrecto`() = runTest {

        val regionId = "7"
        coEvery { mockApiService.fetchAnimalsByRegion(regionId) } returns mockResponse

        repository.getAnimalsByRegion(regionId)

        coVerify(exactly = 1) { mockApiService.fetchAnimalsByRegion(regionId) }

    }

    @Test
    fun `getAnimalsByComuna_llamaAEndpointComunaConParametroCorrecto`() = runTest{

        val comuna = "Viña del Mar"
        coEvery { mockApiService.fetchAnimalsByComuna(comuna) } returns mockResponse

        repository.getAnimalsByComuna(comuna)

        coVerify(exactly = 1) { mockApiService.fetchAnimalsByComuna(comuna) }
    }

    @Test
    fun `getAnimalsByEstado_llamaAEndpointEstadoConParametroCorrecto` () = runTest{

        val estado = "adopcion"
        coEvery { mockApiService.fetchAnimalsByEstado(estado) } returns mockResponse

        repository.getAnimalsByEstado(estado)

        coVerify(exactly = 1) { mockApiService.fetchAnimalsByEstado(estado) }


    }
}