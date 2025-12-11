package com.example.baseproject.data

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.baseproject.model.User
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class UserRepositoryTest {
    //contexto de la app simulada
    private lateinit var context: Application

    private lateinit var repository: UserRepository

    // Datos simulados
    private val TEST_USER =
        User(name = "test_user", password = "SecurePassword123", createdAt = 1000L)
    private val NEW_USER = User(name = "nuevo_user", password = "NewPass456", createdAt = 2000L)

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        repository = UserRepository(context)

        clearSharedPreferences()
    }

    @After
    fun tearDown() {
        clearSharedPreferences()

    }

    private fun clearSharedPreferences() {
        val prefs = context.getSharedPreferences("user_prefs", Application.MODE_PRIVATE)
        // Corregido: No es necesario usar shadowOf para limpiar prefs en Robolectric
        prefs.edit().clear().commit()
    }

    // Tests guardar y recuperar Usuario

    @Test
    fun `saveUser_y_getUser_persisteCorrectamenteTodosLosCampos`() {
        //Usuario de prueba
        val userToSave = TEST_USER

        // se guarda el usuario
        repository.saveUser(userToSave)

        //se verifica si el usuario sea igual al guardado
        val retrievedUser = repository.getUser()

        assertNotNull(retrievedUser)
        assertEquals(userToSave.name, retrievedUser?.name)
        assertEquals(userToSave.password, retrievedUser?.password)
        assertEquals(userToSave.createdAt, retrievedUser?.createdAt)

    }

    // Aqui se va a verificar un usuario existente
    @Test
    fun `hasUser_retornaFalse_cuandoNoHayUsuarioGuardado`() {

        assertFalse(repository.hasUser())

    }

    @Test
    fun `hasUser_retornaTrue_cuandoHayUsuarioGuardado`() {
        // se guarda un usuario
        repository.saveUser(TEST_USER)

        //se verifica si el usuario es correcto
        assertTrue(repository.hasUser())
    }

    // Aqui v a estar la verificación de la contraseña

    @Test
    fun `verifyPassword_retornaTrue_conContrasenaCorrecta`() {

        repository.saveUser(TEST_USER)
        assertTrue(repository.verifyPassword("SecurePassword123"))
    }
    @Test
    fun `verifyPassword_retornaFalse_conContrasenaIncorrecta`(){
        repository.saveUser(TEST_USER)

        assertFalse(repository.verifyPassword("Incorrecta"))

    }

    @Test
    fun `clearUser_eliminaUsuario_yHasUserRetornaFalse`(){
        // Un usuario guardardo
        repository.saveUser(TEST_USER)
        assertTrue(repository.hasUser())

        //Limpiamos el usuario
        repository.clearUser()

        // Verificamos que no haya un usuario
        assertFalse(repository.hasUser())
        assertNull(repository.getUser())

    }
}
