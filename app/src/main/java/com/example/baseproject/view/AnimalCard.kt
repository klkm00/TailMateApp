package com.example.baseproject.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.baseproject.model.Animal

@Composable
fun AnimalCard(animal: Animal, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable(onClick = onClick)
    ){
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