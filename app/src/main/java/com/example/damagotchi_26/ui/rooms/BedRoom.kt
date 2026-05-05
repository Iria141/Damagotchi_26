package com.example.damagotchi_26.ui.rooms

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.damagotchi_26.R
import com.example.damagotchi_26.domain.Pet
import com.example.damagotchi_26.ui.components.IconsPanel
import com.example.damagotchi_26.ui.components.NightOverlay
import com.example.damagotchi_26.ui.theme.ActionButton
import com.example.damagotchi_26.viewmodel.TransicionViewModel
import kotlinx.coroutines.delay

private enum class TipoDescanso { NINGUNO, SIESTA, DORMIR }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BedRoom(
    pet: Pet,
    dormir: () -> Unit,
    siesta: () -> Unit,
    dormirProgresivo: () -> Unit,
    meditar: () -> Unit
) {
    val vm: TransicionViewModel = viewModel()
    val momento by vm.momentoDia.collectAsState()

    var mostrarDialogo by remember { mutableStateOf(false) }
    var tipoDescanso by remember { mutableStateOf(TipoDescanso.NINGUNO) }
    var alpha by remember { mutableStateOf(0f) }

    val alphaAnimado by animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(durationMillis = 1200),
        label = "overlay"
    )

    LaunchedEffect(tipoDescanso) {
        when (tipoDescanso) {
            TipoDescanso.SIESTA -> {
                alpha = 0.65f
                repeat(2) {
                    delay(2000)
                    dormirProgresivo()
                }
                alpha = 0f
                delay(1200)
                tipoDescanso = TipoDescanso.NINGUNO
            }
            TipoDescanso.DORMIR -> {
                alpha = 0.92f
                repeat(5) {
                    delay(2500)
                    dormirProgresivo()
                }
                alpha = 0f
                delay(1200)
                tipoDescanso = TipoDescanso.NINGUNO
            }
            TipoDescanso.NINGUNO -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Dormitorio",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 30.sp
                        )
                        Text(
                            text = "Descanso: ${pet.descanso}%  |  Energia: ${pet.energia}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.dormitorio),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            NightOverlay(momento = momento, maxDarkness = 0.55f)
            IconsPanel(pet = pet)

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(20.dp)
                    .zIndex(10f),
                horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionButton(
                    image = R.drawable.dormido,
                    text = "Descansar",
                    onClick = {
                        if (tipoDescanso == TipoDescanso.NINGUNO) {
                            mostrarDialogo = true
                        }
                    }
                )
            }

            // Overlay SIESTA
            if (tipoDescanso == TipoDescanso.SIESTA && alphaAnimado > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFFFD080).copy(alpha = alphaAnimado))
                        .zIndex(20f),
                    contentAlignment = Alignment.Center
                ) {
                    if (alphaAnimado > 0.3f) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(text = "☀️", fontSize = 44.sp)
                            Text(
                                text = "Echando una siesta...",
                                fontSize = 20.sp,
                                color = Color(0xFF5A3A00),
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                            Text(text = "😴", fontSize = 44.sp)
                        }
                    }
                }
            }

            // Overlay DORMIR
            if (tipoDescanso == TipoDescanso.DORMIR && alphaAnimado > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF0A0A2A).copy(alpha = alphaAnimado))
                        .zIndex(20f),
                    contentAlignment = Alignment.Center
                ) {
                    if (alphaAnimado > 0.5f) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(text = "🌙 ✨ ⭐ ✨ 🌙", fontSize = 32.sp)
                            Text(
                                text = "Durmiendo...",
                                fontSize = 22.sp,
                                color = Color.White.copy(alpha = 0.85f),
                                fontWeight = FontWeight.Light
                            )
                            Text(text = "💤", fontSize = 48.sp)
                        }
                    }
                }
            }

            // Dialogo de eleccion
            if (mostrarDialogo) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                        .zIndex(30f),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Que tipo de descanso?",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF7B3FA0)
                            )

                            Button(
                                onClick = {
                                    mostrarDialogo = false
                                    siesta()
                                    tipoDescanso = TipoDescanso.SIESTA
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFFD080)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Siesta",
                                        fontSize = 16.sp,
                                        color = Color(0xFF5A3A00),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Descanso corto · ~4 segundos",
                                        fontSize = 12.sp,
                                        color = Color(0xFF5A3A00)
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    mostrarDialogo = false
                                    dormir()
                                    tipoDescanso = TipoDescanso.DORMIR
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF0A0A2A)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Dormir",
                                        fontSize = 16.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Sueno profundo · ~12 segundos",
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.7f)
                                    )
                                }
                            }

                            TextButton(onClick = { mostrarDialogo = false }) {
                                Text(text = "Cancelar", color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}