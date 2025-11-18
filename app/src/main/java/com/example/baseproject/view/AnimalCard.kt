package com.example.baseproject.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.baseproject.model.Animal

@Composable
fun AnimalCard(animal: Animal, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable(onClick = onClick)
    ){
        Column {
            AsyncImage(
                model = animal.imagen,
                contentDescription = "Imagen de ${animal.nombre}",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(), // La altura se adapta a la imagen
                contentScale = ContentScale.FillWidth // La imagen llena el ancho sin recortarse
            )
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = animal.nombre,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Tipo: ${animal.tipo}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Estado: ${animal.estado}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (animal.estado.equals("disponible", ignoreCase = true)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}