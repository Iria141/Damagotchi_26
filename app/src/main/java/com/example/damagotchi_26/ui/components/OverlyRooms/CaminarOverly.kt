package com.example.damagotchi_26.ui.components.OverlyRooms

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.damagotchi_26.R
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun CaminarOverlay(
    onCompletado: () -> Unit
) {
    val density = LocalDensity.current
    var posX by remember { mutableStateOf(100f) }
    var haciaDerecha by remember { mutableStateOf(true) }
    var frameAnda by remember { mutableStateOf(true) } // alterna camina/para
    var progreso by remember { mutableStateOf(0f) }
    var completado by remember { mutableStateOf(false) }

    val duracionTotal = 8000L // 8 segundos
    val velocidad = 8f        // px por tick
    val tickMs = 50L          // cada 50ms mueve

    // Selecciona el drawable según dirección y frame
    val drawable = when {
        haciaDerecha && frameAnda -> R.drawable.camina_dcha
        haciaDerecha && !frameAnda -> R.drawable.para_dcha
        !haciaDerecha && frameAnda -> R.drawable.camina_izq
        else -> R.drawable.para_izq
    }

    LaunchedEffect(completado) {
        if (completado) {
            delay(500)
            onCompletado()
        }
    }

    // Bucle de movimiento
    LaunchedEffect(Unit) {
        var tiempoTranscurrido = 0L
        var frameCounter = 0

        while (tiempoTranscurrido < duracionTotal) {
            delay(tickMs)
            tiempoTranscurrido += tickMs
            progreso = tiempoTranscurrido / duracionTotal.toFloat()

            // Alterna frame cada 250ms (5 ticks)
            frameCounter++
            if (frameCounter % 5 == 0) {
                frameAnda = !frameAnda
            }

            // Mueve personaje
            posX += if (haciaDerecha) velocidad else -velocidad
        }

        completado = true
    }

    // Detecta bordes y cambia dirección
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.75f))
            .zIndex(50f)
    ) {
        val anchoPx = with(density) { maxWidth.toPx() }
        val altoPx = with(density) { maxHeight.toPx() }
        val tamPersonajePx = with(density) { 300.dp.toPx() }

        // Comprueba bordes
        LaunchedEffect(posX) {
            if (posX > anchoPx - tamPersonajePx) {
                haciaDerecha = false
                frameAnda = false // para_dcha al girar
            } else if (posX < 0f) {
                haciaDerecha = true
                frameAnda = false // para_izq al girar
            }
        }

        // Personaje caminando
        Image(
            painter = painterResource(drawable),
            contentDescription = "Personaje caminando",
            modifier = Modifier
                .size(500.dp)
                .offset {
                    IntOffset(
                        x = posX.roundToInt(),
                        y = (altoPx * 0.10f).roundToInt()
                    )
                }
        )

        // Progreso y texto
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (completado) "¡Qué paseo! 🌳✨" else "¡Dando un paseo!",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(color = Color.Black.copy(alpha = 0.5f), blurRadius = 4f)
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { progreso },
                modifier = Modifier.fillMaxWidth().height(10.dp),
                color = Color(0xFF66BB6A),
                trackColor = Color.White.copy(alpha = 0.3f)
            )
        }
    }
}