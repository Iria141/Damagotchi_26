package com.example.damagotchi_26.ui.rooms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.damagotchi_26.domain.Pet
import com.example.damagotchi_26.ui.components.IconsPanel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameRoom(
    pet: Pet,
    onJugar: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Salón de juegos", fontWeight = FontWeight.SemiBold) }
            )
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
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🕹️ ¡A jugar!", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))
                    Text("Diversión actual: ${pet.actividad}%")
                }
                // Pet grande (opcional, pero queda bien)
                Text(
                    text = if (pet.estaMal()) "🐣💧" else "🐣",
                    style = MaterialTheme.typography.displayLarge
                )

                Button(onClick = onJugar, modifier = Modifier.fillMaxWidth()) {
                    Text("🎮 Jugar (+diversión)")
                }
            }
        }
    }
}
