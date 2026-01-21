package com.example.damagotchi_26.ui.rooms

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.damagotchi_26.domain.Pet
import com.example.damagotchi_26.ui.components.IconsPanel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LivingRoom(
    pet: Pet,
    comer: () -> Unit,
    jugar: () -> Unit
) {
    val estado = when {
        pet.estaKO() -> "😵 ¡Necesito ayuda ya!"
        pet.estaMal() -> "😟 No me siento muy bien..."
        else -> "😊 ¡Estoy genial!"
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Salón") })
        }
    ) { padding ->

        Row(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ✅ Iconos izquierda
            IconsPanel(pet = pet)
            Spacer(Modifier.width(12.dp))

            // ✅ Contenido principal
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = estado,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            //Personaje centrado en pantalla
            Text(
                text = if (pet.estaMal()) "🐣💧" else "🐣",
                style = MaterialTheme.typography.displayLarge
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = comer, modifier = Modifier.weight(1f)) { Text("Comer") }
            Button(onClick = jugar, modifier = Modifier.weight(1f)) { Text("Jugar") }
            }
        }
    }
}
