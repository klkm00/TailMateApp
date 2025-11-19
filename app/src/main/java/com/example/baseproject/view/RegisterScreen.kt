package com.example.baseproject.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.baseproject.viewmodel.LoginUiState
import com.example.baseproject.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen (
    navController: NavController,
    viewModel: LoginViewModel //el view model del login
){
    val uiState by viewModel.uiState.collectAsState()

    //aqui ponemos variables locales como la hace el profe
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") } //esto es para confirmar la clave


    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val snackbarHostState = remember{ SnackbarHostState() } // este una barra que va dar exito o erro

    LaunchedEffect(uiState) {
        when (uiState) {
            is LoginUiState.RegisterSuccess -> {
                // 1. Mostrar mensaje de éxito
                snackbarHostState.showSnackbar("Cuenta creada. Inicia sesión.")

                // 2. Volver al Login (popBackStack)
                navController.popBackStack()

                // 3. Resetear estado
                viewModel.resetState()
            }
            is LoginUiState.Error -> {
                // Mostrar error si falla
                snackbarHostState.showSnackbar((uiState as LoginUiState.Error).message)
            }
            else -> {}
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Crear Cuenta") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Título (Estilo Material del profe)
            Text(
                text = "Registrarse",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // --- CAMPO NOMBRE ---
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    viewModel.resetState() // Limpiar errores al escribir
                },
                label = { Text("Nombre") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true
            )

            // --- CAMPO CONTRASEÑA ---
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                singleLine = true
            )

            // --- CAMPO CONFIRMAR CONTRASEÑA ---
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Contraseña") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                singleLine = true
            )

            // Mensaje de error visual si las contraseñas no coinciden
            if (password != confirmPassword && confirmPassword.isNotEmpty()) {
                Text(
                    text = "Las contraseñas no coinciden",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Loading Indicator si está cargando
            if (uiState is LoginUiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.padding(bottom = 16.dp))
            }

            // --- BOTÓN REGISTRAR ---
            Button(
                onClick = {
                    // Solo llamamos al ViewModel si las contraseñas coinciden
                    if (password == confirmPassword) {
                        viewModel.register(name, password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                // Deshabilitamos si falta info, no coinciden las pass, o está cargando
                enabled = name.isNotBlank() &&
                        password.isNotBlank() &&
                        password == confirmPassword &&
                        uiState !is LoginUiState.Loading
            ) {
                Text(
                    text = "Crear Cuenta",
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón extra para cancelar (opcional, pero buena práctica)
            TextButton(onClick = { navController.popBackStack() }) {
                Text("Cancelar")
            }
        }
    }


}