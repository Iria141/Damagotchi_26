package com.example.damagotchi_26.ui.components.OverlyRooms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.random.Random

data class Nota(
    val id: Int,
    val columna: Int,
    var y: Float,
    val velocidad: Float,
    var acertada: Boolean = false,
    var perdida: Boolean = false
)

@Composable
fun PianoOverlay(
    onCompletado: () -> Unit,
    onCancelar: () -> Unit
) {
    val density = LocalDensity.current
    val numColumnas = 4
    val objetivo = 20

    val notas = remember { mutableStateListOf<Nota>() }
    var acertadas by remember { mutableStateOf(0) }
    var completado by remember { mutableStateOf(false) }
    var idCounter by remember { mutableStateOf(0) }

    // Colores de cada columna
    val coloresColumnas = listOf(
        Color(0xFFE91E63),
        Color(0xFF9C27B0),
        Color(0xFF2196F3),
        Color(0xFF4CAF50)
    )

    LaunchedEffect(completado) {
        if (completado) {
            delay(600)
            onCompletado()
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f))
            .zIndex(50f)
    ) {
        val anchoPx = with(density) { maxWidth.toPx() }
        val altoPx = with(density) { maxHeight.toPx() }
        val anchoColumna = anchoPx / numColumnas
        val alturaZonaTecla = with(density) { 80.dp.toPx() }

        // Lanza notas progresivamente más rápido
        LaunchedEffect(Unit) {
            var velocidadBase = 5f
            var lanzadas = 0

            while (!completado) {
                delay(900L - (lanzadas * 30L).coerceAtMost(600L))
                if (!completado) {
                    val columna = Random.nextInt(numColumnas)
                    notas.add(
                        Nota(
                            id = idCounter++,
                            columna = columna,
                            y = -80f,
                            velocidad = velocidadBase + Random.nextFloat() * 2f
                        )
                    )
                    lanzadas++
                    velocidadBase = (velocidadBase + 0.2f).coerceAtMost(12f)
                }
            }
        }

        // Mueve notas hacia abajo
        LaunchedEffect(Unit) {
            while (!completado) {
                delay(16)
                notas.replaceAll { nota ->
                    if (!nota.acertada && !nota.perdida) {
                        val nuevaY = nota.y + nota.velocidad
                        nota.copy(
                            y = nuevaY,
                            perdida = nuevaY > altoPx
                        )
                    } else nota
                }
                notas.removeAll { it.perdida || it.acertada }
            }
        }

        // Notas cayendo
        notas.filter { !it.acertada && !it.perdida }.forEach { nota ->
            val xCentro = anchoColumna * nota.columna + anchoColumna / 2f
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = (xCentro - with(density) { 30.dp.toPx() }).roundToInt(),
                            y = nota.y.roundToInt()
                        )
                    }
                    .size(width = 60.dp, height = 70.dp)
                    .background(
                        coloresColumnas[nota.columna].copy(alpha = 0.9f),
                        RoundedCornerShape(8.dp)
                    )
                    .border(2.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            )
        }

        // Teclas del piano abajo
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(80.dp)
        ) {
            repeat(numColumnas) { col ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(2.dp)
                        .background(
                            coloresColumnas[col].copy(alpha = 0.7f),
                            RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                        )
                        .border(
                            2.dp,
                            Color.White.copy(alpha = 0.5f),
                            RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                        )
                        .clickable {
                            if (!completado) {
                                // Busca nota más cercana a la zona de la tecla
                                val notaCercana = notas
                                    .filter { !it.acertada && !it.perdida && it.columna == col }
                                    .minByOrNull { kotlin.math.abs(it.y - (altoPx - alturaZonaTecla)) }

                                if (notaCercana != null &&
                                    notaCercana.y > altoPx - alturaZonaTecla * 2f &&
                                    notaCercana.y < altoPx - 20f
                                ) {
                                    val idx = notas.indexOf(notaCercana)
                                    if (idx >= 0) {
                                        notas[idx] = notaCercana.copy(acertada = true)
                                        acertadas++
                                        if (acertadas >= objetivo) completado = true
                                    }
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = listOf("DO", "RE", "MI", "FA")[col],
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // HUD arriba
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (completado) "¡Bravo! 🎵✨"
                else "¡Toca cuando llegue la nota! ($acertadas/$objetivo)",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(color = Color.Black.copy(alpha = 0.5f), blurRadius = 4f)
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { acertadas / objetivo.toFloat() },
                modifier = Modifier.fillMaxWidth().height(10.dp),
                color = Color(0xFFE91E63),
                trackColor = Color.White.copy(alpha = 0.2f)
            )
        }

        TextButton(
            onClick = onCancelar,
            modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 90.dp, end = 8.dp)
        ) {
            Text(
                "Cancelar",
                color = Color.White.copy(alpha = 0.6f),
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(color = Color.Black.copy(alpha = 0.5f), blurRadius = 4f)
                )
            )
        }
    }
}