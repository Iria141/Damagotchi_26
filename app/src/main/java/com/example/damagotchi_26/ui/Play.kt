package com.example.damagotchi_26.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayScreen(
    volver: () -> Unit,
    jugar: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Juego") },
                navigationIcon = {
                    TextButton(onClick = volver) { Text("Volver") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Minijuego simple (por ahora):")
            Spacer(Modifier.height(16.dp))

            Button(onClick = jugar) { Text("🎮 Jugar (+diversión)") }
        }
    }
}
