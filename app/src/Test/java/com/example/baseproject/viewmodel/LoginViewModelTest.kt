package com.example.baseproject.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.baseproject.data.UserRepository
import com.example.baseproject.model.User
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
// imports para que se hagan las pruebitas


// uitlisa

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {



    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher() // controla la ejecucion de corrutinas

    private val mockApplication = mockk<Application>(relaxed = true) // simula los mocks

    private lateinit var mockRepository: UserRepository // esto va a simular el UserRepository

    private lateinit var viewModel: LoginViewModel // esta es la clase que se va a probar

    private val TEST_USER = User(name = "testuser", password = "testpassword") // datos simulados

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        //Iniciar el mmock del repositorio
        mockRepository = mockk(relaxed = true)

        //Aqui el viewmodel uitlizara los mocks en ves de los datos reales
        viewModel = LoginViewModel(mockApplication, mockRepository)


        coEvery { mockRepository.getUser() } returns TEST_USER
        coEvery { mockRepository.verifyPassword(TEST_USER.password) } returns true

    }
    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `Prueba_de_camposVacios_retornara_error`() = runTest(testDispatcher) {

       //GIVEN = aqui se daran los datos para hace los test
        val usernameVacio = ""
        val passwordValida = "pass123"
        //When se llama a la función login
        viewModel.login(usernameVacio, passwordValida)

        //THEN : El estado es error
        val uiState = viewModel.uiState.first()
        assertTrue(uiState is LoginUiState.Error)
        assertEquals("Por favor, completa todos los campos", (uiState as LoginUiState.Error).message)

        // basicamente es como un porcedimiento de GIVEN -> WHEN -> THEN por lo que entiendo
    }

    @Test
    fun `LoginCorrecto_retrona_succes`() = runTest(testDispatcher) {

        viewModel.login("usuario_falso", TEST_USER.password)

        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiState.first()
        assertTrue(uiState is LoginUiState.Error)
        assertEquals("Usuario o contraseña incorrectos", (uiState as LoginUiState.Error).message)

    }


    @Test
    fun `registro_credencialesValidas_retornaRegisterSuccess`() = runTest(testDispatcher) {

        coEvery { mockRepository.getUser() } returns null // se asegura que no jay un usuario existente

        val newUser = "usuario_nuevo"
        val newPass = "pass_nuevo"

        viewModel.register(newUser, newPass)

        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiState.first()
        assertTrue(uiState is LoginUiState.RegisterSuccess)

    }

    @Test
    fun `registro_usuarioYaExistente_retornaError`() = runTest(testDispatcher) {
        // esto va a simular como que un usuario ya existe
        coEvery { mockRepository.getUser() } returns TEST_USER

        viewModel.register(TEST_USER.name, "otrapass")

        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiState.first()
        assertTrue(uiState is LoginUiState.Error)
        assertEquals("El usuario ya existe", (uiState as LoginUiState.Error).message)


    }
}
