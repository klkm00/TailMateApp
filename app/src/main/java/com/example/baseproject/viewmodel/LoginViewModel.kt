package com.example.baseproject.viewmodel


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baseproject.data.UserRepository
import com.example.baseproject.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.AndroidViewModel
import com.example.baseproject.data.AnimalRepository


sealed class LoginUiState {
    object Idle : LoginUiState()               // Esperando
    object Loading : LoginUiState()            // cargando
    data class Success(val user: User) : LoginUiState() // correcto
    data class Error(val message: String) : LoginUiState() // problema

    object RegisterSuccess : LoginUiState()// Registro exitoso

}

class LoginViewModel(application: Application , //esto de aqui va a llamar los valores por defecto
                     private val repository: UserRepository = UserRepository(application.applicationContext)) : AndroidViewModel(application) { // Esto crea objetos tipo mocks para hacer las pruebas

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    //Fun para iniciar sesion - chris 
    fun login(username: String, password: String){
        //Éstas son las validaciones 
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState.Error("Por favor, completa todos los campos")
            return //Por lo que logro entender es que para la validación si es que el usuario y  la contraseña se encuentran en blanco va a tirar el estado de error con el mensaje 
        }
        _uiState.value = LoginUiState.Loading //Este es el estado en el que sale cargando 

        viewModelScope.launch { //Dentro de esta función lo que se hace es simular una carga de 2 segundos 
            delay(2000L)
            
            try {
                val storedUser = repository.getUser() //La variable storeuser es la que estará trayendo el usuario guardado en el repositorio localmente 

                if (storedUser != null && storedUser.name == username && repository.verifyPassword(password)) {
                    _uiState.value = LoginUiState.Success(storedUser) //Básicamente si aquí los datos ingresados no son nulos y son iguales a los datos en el storeuser retorna el estado de succes 
                } else {
                    _uiState.value = LoginUiState.Error("Usuario o contraseña incorrectos") //Aquí lo contrario 
                }
            } catch (e : Exception) {
                _uiState.value = LoginUiState.Error("Error al iniciar sesión")
            }
        }
          
    }
    
    
    fun register(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState.Error("Por favor, completa todos los campos")
            return
        }

        _uiState.value = LoginUiState.Loading
        
        viewModelScope.launch {
            delay(2000L)

            try {
                val existingUser = repository.getUser()
                if (existingUser != null && existingUser.name == username) {
                    _uiState.value = LoginUiState.Error("El usuario ya existe")
                } else {
                val newUser = User(name = username, password = password) //Creo un nuevo usuario
                repository.saveUser(newUser) //Lo guardo en el repositorio local 
                _uiState.value = LoginUiState.RegisterSuccess //Y retorno el estado de success Por lo que no será necesario iniciar sesión después de crearlo
                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error("Error al registrar usuario")
            }
        }
    }
    
    //Esta función lo que va a hacer es volver al estado inicial 
    fun resetState(){
        _uiState.value = LoginUiState.Idle
    }
    
    fun logout(){
        repository.clearUser()
        _uiState.value = LoginUiState.Idle
    }

}
