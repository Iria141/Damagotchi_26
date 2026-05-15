package com.example.damagotchi_26.ui.components.OverlyRooms

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
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
    sonidosActivados: Boolean = true,
    onCompletado: () -> Unit
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.pasos).apply {
            isLooping = true
        }
    }

    LaunchedEffect(Unit) {
        if (sonidosActivados) mediaPlayer.start()
    }

    DisposableEffect(Unit) {
        onDispose {
            if (mediaPlayer.isPlaying) mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    var posX by remember { mutableStateOf(100f) }
    var haciaDerecha by remember { mutableStateOf(true) }
    var frameAnda by remember { mutableStateOf(true) }
    var progreso by remember { mutableStateOf(0f) }
    var completado by remember { mutableStateOf(false) }

    val duracionTotal = 8000L
    val velocidad = 8f
    val tickMs = 50L

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

    LaunchedEffect(Unit) {
        var tiempoTranscurrido = 0L
        var frameCounter = 0
        while (tiempoTranscurrido < duracionTotal) {
            delay(tickMs)
            tiempoTranscurrido += tickMs
            progreso = tiempoTranscurrido / duracionTotal.toFloat()
            frameCounter++
            if (frameCounter % 5 == 0) frameAnda = !frameAnda
            posX += if (haciaDerecha) velocidad else -velocidad
        }
        completado = true
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.75f))
            .zIndex(50f)
    ) {
        val anchoPx = with(density) { maxWidth.toPx() }
        val altoPx = with(density) { maxHeight.toPx() }
        val tamPersonajePx = with(density) { 300.dp.toPx() }

        LaunchedEffect(posX) {
            if (posX > anchoPx - tamPersonajePx) {
                haciaDerecha = false
                frameAnda = false
            } else if (posX < 0f) {
                haciaDerecha = true
                frameAnda = false
            }
        }

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