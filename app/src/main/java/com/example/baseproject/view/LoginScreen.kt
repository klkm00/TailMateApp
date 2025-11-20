package com.example.baseproject.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider
import com.example.baseproject.data.UserRepository
import com.example.baseproject.viewmodel.LoginUiState
import com.example.baseproject.viewmodel.LoginViewModel


@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit, // Callback para navegar cuando el login es exitoso
    onNavigateToRegister: () -> Unit
) {

    val viewModel: LoginViewModel = viewModel()

    //Esto hace que se observe el estado en que se encuentra el login
    val uiState by viewModel.uiState.collectAsState()

    //Sola variable locales
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Aquí es el estado si el estado esta bien, lo limpia  Y navega
    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            onLoginSuccess()
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Bienvenido a TailMate",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        //Este es el campo donde irá el usuario
        OutlinedTextField(
            value = username,
            onValueChange = { username = it
                            viewModel.resetState()},
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))


        //Aqui va la contraseña y el boton de login
        OutlinedTextField(
            value = password,
            onValueChange = { password = it
                            viewModel.resetState()},
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            enabled = uiState !is LoginUiState.Loading,
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = null)
                }
            }
        )

        //Aquí se muestra los mensajes de errores y que existen
        if (uiState is LoginUiState.Error) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = (uiState as LoginUiState.Error).message,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
        }



        Spacer(modifier = Modifier.height(32.dp))

        // Este es el botón principal el cual es para ingresarasda

        Button(
            onClick = {
                viewModel.login(username, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = uiState !is LoginUiState.Loading
        ) {
            if (uiState is LoginUiState.Loading){
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            }else{
                Text("Ingresar", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Boton secundario (Registrarse)
        OutlinedButton(
            onClick = {
                onNavigateToRegister()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = true
        ){
            Text("Registrarse", fontSize = 16.sp)
        }

    }
}