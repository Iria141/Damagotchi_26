package com.example.damagotchi_26.ui.rooms

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
import kotlin.random.Random

@Composable
fun DientesOverlay(
    onCompletado: () -> Unit,
    onCancelar: () -> Unit
) {
    val density = LocalDensity.current
    var progreso by remember { mutableStateOf(0f) }
    var cepilloPos by remember { mutableStateOf(Offset(400f, 600f)) }
    val espumaPoints = remember { mutableStateListOf<Offset>() }
    var completado by remember { mutableStateOf(false) }
    var frotadas by remember { mutableStateOf(0) }
    var ultimaDireccion by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(80)
            if (espumaPoints.size > 40) espumaPoints.removeAt(0)
        }
    }

    LaunchedEffect(completado) {
        if (completado) {
            delay(1000)
            onCompletado()
        }
    }

    Box(modifier = Modifier.fillMaxSize().zIndex(50f)) {

        // Área arrastre transparente
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset -> cepilloPos = offset },
                        onDrag = { change, dragAmount ->
                            if (!completado) {
                                cepilloPos = change.position

                                // Detectar cambio de dirección horizontal
                                if (ultimaDireccion != 0f && dragAmount.x * ultimaDireccion < 0) {
                                    frotadas++
                                    progreso = (frotadas / 10f).coerceAtMost(1f)
                                    if (progreso >= 1f) completado = true
                                }
                                ultimaDireccion = dragAmount.x

                                espumaPoints.add(
                                    Offset(
                                        cepilloPos.x + Random.nextFloat() * 30f - 15f,
                                        cepilloPos.y + Random.nextFloat() * 20f - 10f
                                    )
                                )
                            }
                        }
                    )
                }
        )

        // Espuma
        Canvas(modifier = Modifier.fillMaxSize()) {
            espumaPoints.forEach { punto ->
                drawCircle(
                    color = Color.White.copy(alpha = 0.85f),
                    radius = Random.nextFloat() * 8f + 4f,
                    center = punto
                )
            }
        }

        // Cepillo siguiendo el dedo exacto
        Image(
            painter = painterResource(R.drawable.cepillo),
            contentDescription = "Cepillo de dientes",
            modifier = Modifier
                .size(80.dp)
                .offset {
                    IntOffset(
                        x = (cepilloPos.x - with(density) { 40.dp.toPx() }).roundToInt(),
                        y = (cepilloPos.y - with(density) { 40.dp.toPx() }).roundToInt()
                    )
                }
        )

        // Progreso
        Column(
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 8.dp).padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (completado) "¡Dientes limpios! 😁✨" else "¡Frota de lado a lado! ($frotadas/10)",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                style = LocalTextStyle.current.copy(shadow = Shadow(color = Color.Black.copy(alpha = 0.5f), blurRadius = 4f))
            )
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { progreso },
                modifier = Modifier.fillMaxWidth().height(10.dp),
                color = Color(0xFF4488FF),
                trackColor = Color.White.copy(alpha = 0.3f)
            )
        }

        TextButton(onClick = onCancelar, modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)) {
            Text("Cancelar", color = Color.White, style = LocalTextStyle.current.copy(shadow = Shadow(color = Color.Black.copy(alpha = 0.5f), blurRadius = 4f)))
        }
    }
}