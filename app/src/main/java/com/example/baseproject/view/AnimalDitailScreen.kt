package com.example.baseproject.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.baseproject.model.Animal
import com.example.baseproject.viewmodel.AnimalViewModel
import java.util.regex.Pattern

// Función auxiliar para limpiar el texto HTML
fun String.htmlToString(): String {
    return Pattern.compile("<[^>]*>").matcher(this).replaceAll("")
}

@Composable
fun AnimalDetailScreen(
    animalId: Int,
    viewModel: AnimalViewModel = viewModel()
) {
    LaunchedEffect(animalId) {
        viewModel.cargaDetalleAnimal(animalId)
    }

    val uiState = viewModel.detailUiState

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.error != null) {
            Text(text = "Error: ${uiState.error}")
        } else if (uiState.animal != null) {
            AnimalDetailsContent(uiState.animal)
        }
    }
}

@Composable
fun AnimalDetailsContent(animal: Animal) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                AsyncImage(
                    model = animal.imagen,
                    contentDescription = "Imagen de ${animal.nombre}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = animal.nombre, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(label = "Estado", value = animal.estado)
            InfoRow(label = "Edad", value = animal.edad ?: "N/A")
            InfoRow(label = "Género", value = animal.genero ?: "N/A")
            InfoRow(label = "Ubicación", value = "${animal.comuna ?: ""}, ${animal.region ?: ""}")
            Spacer(modifier = Modifier.height(16.dp))
            
            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Salud", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(label = "Esterilizado", value = if (animal.esterilizado == 1) "Sí" else "No")
            InfoRow(label = "Vacunas al día", value = if (animal.vacunas == 1) "Sí" else "No")
            
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            if (!animal.descripcionFisica.isNullOrBlank()) {
                Text(text = "Descripción Física", style = MaterialTheme.typography.titleLarge)
                Text(text = animal.descripcionFisica.htmlToString())
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            if (!animal.descripcionPersonalidad.isNullOrBlank()) {
                Text(text = "Personalidad", style = MaterialTheme.typography.titleLarge)
                Text(text = animal.descripcionPersonalidad.htmlToString())
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (!animal.descripcionAdicional.isNullOrBlank()) {
                Text(text = "Información Adicional", style = MaterialTheme.typography.titleLarge)
                Text(text = animal.descripcionAdicional.htmlToString())
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = "$label: ", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
    Spacer(modifier = Modifier.height(4.dp))
}
