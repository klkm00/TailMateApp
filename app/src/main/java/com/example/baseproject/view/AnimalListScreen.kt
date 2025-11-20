package com.example.baseproject.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.baseproject.ui.screens.AppScreen
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
    
    // Estados para los filtros
    var selectedType by remember { mutableStateOf("Todos") }
    var selectedEstado by remember { mutableStateOf("Todos") }
    var comunaSearch by remember { mutableStateOf("") }

    val tipos = listOf("Todos", "Perro", "Gato", "Conejo", "Ave", "Roedor")
    
    // Corregido: Solo los estados que soporta la API
    val estados = listOf("Todos", "adopcion", "encontrado", "perdido")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TailMate") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 1. Buscador por Comuna
            OutlinedTextField(
                value = comunaSearch,
                onValueChange = { comunaSearch = it },
                label = { Text("Buscar por Comuna (ID o Nombre)") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (comunaSearch.isNotBlank()) {
                            selectedType = "Todos"
                            selectedEstado = "Todos"
                            viewModel.filtrarPorComuna(comunaSearch)
                        } else {
                            viewModel.limpiarFiltros()
                        }
                    }
                )
            )

            // 2. Filtro por tipo
            Text(
                text = "Filtrar por Tipo:",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tipos) { tipo ->
                    FilterChip(
                        selected = selectedType == tipo,
                        onClick = {
                            selectedType = tipo
                            selectedEstado = "Todos" // Resetear estado
                            comunaSearch = "" // Resetear búsqueda
                            if (tipo == "Todos") {
                                viewModel.limpiarFiltros()
                            } else {
                                viewModel.filtrarPorTipo(tipo)
                            }
                        },
                        label = { Text(tipo) }
                    )
                }
            }

            // 3. Filtro por ESTADO
            Text(
                text = "Filtrar por Estado:",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(estados) { estado ->
                    FilterChip(
                        selected = selectedEstado == estado,
                        onClick = {
                            selectedEstado = estado
                            selectedType = "Todos" // Resetear tipo
                            comunaSearch = "" // esto hacc que se reseta la comuna
                            if (estado == "Todos") {
                                viewModel.limpiarFiltros()
                            } else {
                                viewModel.filtrarPorEstado(estado)
                            }
                        },
                        label = { Text(estado) }
                    )
                }
            }
            
            Divider()


            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else if (uiState.error != null) {
                    Text(text = "Error o sin resultados: ${uiState.error}")
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.animales) { animal ->
                            AnimalCard(animal = animal, onClick = {
                                navController.navigate(AppScreen.AnimalDetailScreen.createRoute(animal.id.toString()))
                            })
                        }
                    }
                }
            }
        }
    }
}