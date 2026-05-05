package com.example.damagotchi_26.ui.components.OverlyRooms

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay

data class Trazo(
    val puntos: List<Offset>,
    val color: Color,
    val grosor: Float
)

@Composable
fun PintarOverlay(
    onCompletado: () -> Unit,
    onCancelar: () -> Unit
) {
    val density = LocalDensity.current

    val coloresPaleta = listOf(
        Color(0xFFE91E63), // rosa
        Color(0xFF9C27B0), // morado
        Color(0xFF2196F3), // azul
        Color(0xFF4CAF50), // verde
        Color(0xFFFF9800), // naranja
        Color(0xFFFFEB3B), // amarillo
        Color(0xFF795548), // marrón
        Color.White
    )

    var colorSeleccionado by remember { mutableStateOf(coloresPaleta[0]) }
    val trazos = remember { mutableStateListOf<Trazo>() }
    var trazoActual by remember { mutableStateOf<MutableList<Offset>>(mutableListOf()) }
    var progreso by remember { mutableStateOf(0f) }
    var completado by remember { mutableStateOf(false) }
    var totalPuntos by remember { mutableStateOf(0) }

    LaunchedEffect(completado) {
        if (completado) {
            delay(800)
            onCompletado()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .zIndex(50f)
    ) {
        // Área de dibujo
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 140.dp)
                .pointerInput(colorSeleccionado) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            trazoActual = mutableListOf(offset)
                        },
                        onDrag = { change, _ ->
                            if (!completado) {
                                trazoActual.add(change.position)

                                // Actualizar progreso
                                totalPuntos++
                                progreso = (totalPuntos / 800f).coerceAtMost(1f)
                                if (progreso >= 1f && !completado) completado = true
                            }
                        },
                        onDragEnd = {
                            if (trazoActual.size > 1) {
                                trazos.add(
                                    Trazo(
                                        puntos = trazoActual.toList(),
                                        color = colorSeleccionado,
                                        grosor = 18f
                                    )
                                )
                            }
                            trazoActual = mutableListOf()
                        }
                    )
                }
        ) {
            // Fondo lienzo
            drawRect(color = Color(0xFFFFFDE7))

            // Trazos guardados
            trazos.forEach { trazo ->
                if (trazo.puntos.size > 1) {
                    val path = Path()
                    path.moveTo(trazo.puntos[0].x, trazo.puntos[0].y)
                    trazo.puntos.drop(1).forEach { punto ->
                        path.lineTo(punto.x, punto.y)
                    }
                    drawPath(
                        path = path,
                        color = trazo.color,
                        style = Stroke(
                            width = trazo.grosor,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }
            }

            // Trazo actual
            if (trazoActual.size > 1) {
                val path = Path()
                path.moveTo(trazoActual[0].x, trazoActual[0].y)
                trazoActual.drop(1).forEach { punto ->
                    path.lineTo(punto.x, punto.y)
                }
                drawPath(
                    path = path,
                    color = colorSeleccionado,
                    style = Stroke(
                        width = 18f,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }

        // Barra de progreso y texto
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (completado) "¡Obra de arte! 🎨✨" else "¡Pinta libremente!",
                color = Color(0xFF7B3FA0),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { progreso },
                modifier = Modifier.fillMaxWidth().height(10.dp),
                color = colorSeleccionado,
                trackColor = Color.LightGray.copy(alpha = 0.4f)
            )
        }

        // Panel inferior — paleta de colores + botones
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5))
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Paleta de colores
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                coloresPaleta.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(if (color == colorSeleccionado) 36.dp else 28.dp)
                            .background(color, CircleShape)
                            .border(
                                width = if (color == colorSeleccionado) 3.dp else 1.dp,
                                color = Color(0xFF7B3FA0),
                                shape = CircleShape
                            )
                            .clickable { colorSeleccionado = color }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Borrar último trazo
                TextButton(
                    onClick = { if (trazos.isNotEmpty()) trazos.removeAt(trazos.lastIndex) }
                ) {
                    Text("↩ Deshacer", color = Color(0xFF7B3FA0), fontWeight = FontWeight.SemiBold)
                }

                // Cancelar
                TextButton(onClick = onCancelar) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        }
    }
}