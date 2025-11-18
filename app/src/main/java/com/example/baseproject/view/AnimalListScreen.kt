package com.example.baseproject.view // Corrected package

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.baseproject.viewmodel.AnimalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalListScreen (
    navController: NavController,
    viewModel: AnimalViewModel = viewModel()
){
    LaunchedEffect(Unit) {
        viewModel.cargarListaAnimales()
    }

    val uiState = viewModel.listUiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adopta un Amigo") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else if (uiState.error != null) {
                Text(text = "Error al cargar los datos: ${uiState.error}")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.animales) { animal ->
                        AnimalCard(animal = animal, onClick = {
                            // TODO: Navegar a la pantalla de detalles del animal.
                            // Por ejemplo: navController.navigate("animalDetail/${animal.id}")
                        })
                    }
                }
            }
        }
    }
}///aaa