package com.example.damagotchi_26.ui.rooms

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.damagotchi_26.R
import kotlinx.coroutines.delay

@Composable
fun EstirarOverlay(
    onCompletado: () -> Unit
) {
    val secuencia = listOf(
        R.drawable.costado_izq_abierto,
        R.drawable.estira_izq,
        R.drawable.costado_dcha_abierto,
        R.drawable.estira_dcha
    )

    val objetivo = 8
    var frameActual by remember { mutableStateOf(0) }
    var deslizamientos by remember { mutableStateOf(0) }
    var completado by remember { mutableStateOf(false) }
    var arrastreAcumulado by remember { mutableStateOf(0f) }
    var haciaDerecha by remember { mutableStateOf(true) }

    val progreso = deslizamientos / objetivo.toFloat()

    LaunchedEffect(completado) {
        if (completado) {
            delay(600)
            onCompletado()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.75f))
            .zIndex(50f)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        arrastreAcumulado = 0f
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        if (!completado) {
                            arrastreAcumulado += dragAmount

                            // Umbral para contar como deslizamiento
                            if (arrastreAcumulado > 80f) {
                                haciaDerecha = true
                                frameActual = (frameActual + 1) % secuencia.size
                                deslizamientos++
                                arrastreAcumulado = 0f
                                if (deslizamientos >= objetivo) completado = true
                            } else if (arrastreAcumulado < -80f) {
                                haciaDerecha = false
                                frameActual = (frameActual - 1 + secuencia.size) % secuencia.size
                                deslizamientos++
                                arrastreAcumulado = 0f
                                if (deslizamientos >= objetivo) completado = true
                            }
                        }
                    }
                )
            }
    ) {
        // Personaje con animación de entrada según dirección
        AnimatedContent(
            targetState = frameActual,
            transitionSpec = {
                if (haciaDerecha) {
                    slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                } else {
                    slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
                }
            },
            label = "pose",
            modifier = Modifier.align(Alignment.Center)
        ) { frame ->
            Image(
                painter = painterResource(secuencia[frame]),
                contentDescription = "Estiramiento",
                modifier = Modifier.size(600.dp)
            )
        }

        // Progreso y texto
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (completado) "¡Genial! 🤸✨"
                else "¡Desliza para estirar! ($deslizamientos/$objetivo)",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(color = Color.Black.copy(alpha = 0.5f), blurRadius = 4f)
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { progreso.coerceAtMost(1f) },
                modifier = Modifier.fillMaxWidth().height(10.dp),
                color = Color(0xFFAB47BC),
                trackColor = Color.White.copy(alpha = 0.3f)
            )
        }

        // Flechas indicadoras
        if (!completado && deslizamientos == 0) {
            Text(
                text = "← desliza →",
                fontSize = 18.sp,
                color = Color.White.copy(alpha = 0.7f),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 120.dp),
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(color = Color.Black.copy(alpha = 0.5f), blurRadius = 4f)
                )
            )
        }
    }
}