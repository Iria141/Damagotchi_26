package com.example.damagotchi_26.ui.components.OverlyRooms

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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

@Composable
fun PelotaOverlay(
    onCompletado: () -> Unit
) {
    val density = LocalDensity.current
    val duracionTotal = 15000L
    var tiempoRestante by remember { mutableStateOf(15) }
    var completado by remember { mutableStateOf(false) }

    // Posición y velocidad de la pelota
    var posX by remember { mutableStateOf(300f) }
    var posY by remember { mutableStateOf(400f) }
    var velX by remember { mutableStateOf(8f) }
    var velY by remember { mutableStateOf(6f) }

    LaunchedEffect(completado) {
        if (completado) {
            delay(600)
            onCompletado()
        }
    }

    // Countdown
    LaunchedEffect(Unit) {
        repeat(15) {
            delay(1000)
            tiempoRestante--
        }
        completado = true
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f))
            .zIndex(50f)
    ) {
        val anchoPx = with(density) { maxWidth.toPx() }
        val altoPx = with(density) { maxHeight.toPx() }
        val tamPelotaPx = with(density) { 80.dp.toPx() }

        // Física de rebote
        LaunchedEffect(Unit) {
            while (!completado) {
                delay(16)
                posX += velX
                posY += velY

                // Rebote horizontal
                if (posX <= 0f) {
                    posX = 0f
                    velX = kotlin.math.abs(velX)
                } else if (posX >= anchoPx - tamPelotaPx) {
                    posX = anchoPx - tamPelotaPx
                    velX = -kotlin.math.abs(velX)
                }

                // Rebote vertical
                if (posY <= 0f) {
                    posY = 0f
                    velY = kotlin.math.abs(velY)
                } else if (posY >= altoPx - tamPelotaPx) {
                    posY = altoPx - tamPelotaPx
                    velY = -kotlin.math.abs(velY)
                }
            }
        }

        // Área de arrastre — al tocar la pelota la empujas
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            val dedoX = change.position.x
                            val dedoY = change.position.y
                            val centroPelotaX = posX + tamPelotaPx / 2
                            val centroPelotaY = posY + tamPelotaPx / 2
                            val distX = dedoX - centroPelotaX
                            val distY = dedoY - centroPelotaY
                            val distancia = kotlin.math.sqrt(distX * distX + distY * distY)

                            // Solo empuja si el dedo está cerca de la pelota
                            if (distancia < tamPelotaPx * 1.5f) {
                                velX += dragAmount.x * 0.3f
                                velY += dragAmount.y * 0.3f
                                // Limitar velocidad máxima
                                velX = velX.coerceIn(-18f, 18f)
                                velY = velY.coerceIn(-18f, 18f)
                            }
                        }
                    )
                }
        )

        // Pelota
        Image(
            painter = painterResource(R.drawable.pelota),
            contentDescription = "Pelota",
            modifier = Modifier
                .size(80.dp)
                .offset {
                    IntOffset(
                        x = posX.roundToInt(),
                        y = posY.roundToInt()
                    )
                }
        )

        // HUD arriba
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (completado) "¡Bien jugado! ⚽✨"
                else "¡Juega con la pelota! $tiempoRestante\"",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(color = Color.Black.copy(alpha = 0.5f), blurRadius = 4f)
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { tiempoRestante / 15f },
                modifier = Modifier.fillMaxWidth().height(10.dp),
                color = Color(0xFF66BB6A),
                trackColor = Color.White.copy(alpha = 0.3f)
            )
        }
    }
}