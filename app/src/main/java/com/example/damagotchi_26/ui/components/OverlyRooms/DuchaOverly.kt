package com.example.damagotchi_26.ui.rooms

import android.media.MediaPlayer
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.pointer.pointerInput
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
import kotlin.math.sqrt
import kotlin.random.Random

data class Burbuja(
    val x: Float,
    val y: Float,
    val radio: Float,
    var alpha: Float = 1f
)

@Composable
fun DuchaOverlay(
    sonidosActivados: Boolean = true,
    onCompletado: () -> Unit,
    onCancelar: () -> Unit
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    var progreso by remember { mutableStateOf(0f) }
    var esponjaPos by remember { mutableStateOf(Offset(400f, 600f)) }
    val burbujas = remember { mutableStateListOf<Burbuja>() }
    var completado by remember { mutableStateOf(false) }

    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.agua).apply {
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

    LaunchedEffect(Unit) {
        while (true) {
            delay(50)
            burbujas.replaceAll { it.copy(alpha = it.alpha - 0.04f) }
            burbujas.removeAll { it.alpha <= 0f }
        }
    }

    LaunchedEffect(completado) {
        if (completado) {
            delay(1000)
            onCompletado()
        }
    }

    Box(modifier = Modifier.fillMaxSize().zIndex(50f)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset -> esponjaPos = offset },
                        onDrag = { change, dragAmount ->
                            if (!completado) {
                                esponjaPos = change.position
                                repeat(3) {
                                    burbujas.add(
                                        Burbuja(
                                            x = esponjaPos.x + Random.nextFloat() * 50f - 25f,
                                            y = esponjaPos.y + Random.nextFloat() * 50f - 25f,
                                            radio = Random.nextFloat() * 16f + 6f
                                        )
                                    )
                                }
                                val dist = sqrt(dragAmount.x * dragAmount.x + dragAmount.y * dragAmount.y)
                                progreso = (progreso + dist / 4500f).coerceAtMost(1f)
                                if (progreso >= 1f && !completado) completado = true
                            }
                        }
                    )
                }
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            burbujas.forEach { b ->
                drawCircle(color = Color.White.copy(alpha = b.alpha * 0.75f), radius = b.radio, center = Offset(b.x, b.y))
                drawCircle(color = Color(0xFFAADDFF).copy(alpha = b.alpha * 0.4f), radius = b.radio * 0.55f, center = Offset(b.x - b.radio * 0.2f, b.y - b.radio * 0.2f))
            }
        }

        Image(
            painter = painterResource(R.drawable.esponja),
            contentDescription = "Esponja",
            modifier = Modifier
                .size(70.dp)
                .offset {
                    IntOffset(
                        x = (esponjaPos.x - with(density) { -20.dp.toPx() }).roundToInt(),
                        y = (esponjaPos.y - with(density) { -80.dp.toPx() }).roundToInt()
                    )
                }
        )

        Column(
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 8.dp).padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (completado) "¡Limpia! 🚿✨" else "¡Frota la esponja!",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                style = LocalTextStyle.current.copy(shadow = Shadow(color = Color.Black.copy(alpha = 0.5f), blurRadius = 4f))
            )
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { progreso },
                modifier = Modifier.fillMaxWidth().height(10.dp),
                color = Color(0xFF00DDAA),
                trackColor = Color.White.copy(alpha = 0.3f)
            )
        }

        TextButton(onClick = onCancelar, modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)) {
            Text("Cancelar", color = Color.White, style = LocalTextStyle.current.copy(shadow = Shadow(color = Color.Black.copy(alpha = 0.5f), blurRadius = 4f)))
        }
    }
}