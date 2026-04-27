package com.example.damagotchi_26.ui.components.OverlyRooms

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
import kotlin.math.sqrt
import androidx.compose.foundation.background

@Composable
fun ComerOverlay(
    onCompletado: () -> Unit,
    onCancelar: () -> Unit
) {
    val density = LocalDensity.current
    var cucharaPos by remember { mutableStateOf(Offset(300f, 900f)) }
    var cucharaLlena by remember { mutableStateOf(true) }
    var completado by remember { mutableStateOf(false) }
    var progreso by remember { mutableStateOf(0f) }

    // Centro aproximado de la boca del personaje (parte superior central de la pantalla)
    // Se ajusta en tiempo de composición según el tamaño real
    var bocaTarget by remember { mutableStateOf(Offset(540f, 400f)) }

    LaunchedEffect(completado) {
        if (completado) {
            delay(800)
            onCompletado()
        }
    }

    BoxWithConstraints(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black.copy(alpha = 0.35f))
        .zIndex(50f)
    ) {
        val anchoPx = with(density) { maxWidth.toPx() }
        val altoPx = with(density) { maxHeight.toPx() }

        // Boca aprox en el centro horizontal, 35% desde arriba
        bocaTarget = Offset(anchoPx / 2f, altoPx * 0.35f)

        // Posición inicial de la cuchara — abajo al centro
        LaunchedEffect(Unit) {
            cucharaPos = Offset(anchoPx / 2f, altoPx * 0.80f)
        }

        // Área de arrastre
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset -> cucharaPos = offset },
                        onDrag = { change, _ ->
                            if (!completado && cucharaLlena) {
                                cucharaPos = change.position

                                // Calcular distancia a la boca
                                val dx = cucharaPos.x - bocaTarget.x
                                val dy = cucharaPos.y - bocaTarget.y
                                val dist = sqrt(dx * dx + dy * dy)

                                // Si llega cerca de la boca
                                if (dist < 120f) {
                                    cucharaLlena = false
                                    completado = true
                                }

                                // Progreso inversamente proporcional a la distancia
                                val distanciaMaxima = sqrt(anchoPx * anchoPx + altoPx * altoPx)
                                progreso = (1f - (dist / distanciaMaxima)).coerceIn(0f, 1f)
                            }
                        }
                    )
                }
        )

        // Indicador visual de la boca (zona objetivo)
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.White.copy(alpha = 0.25f),
                radius = 120f,
                center = bocaTarget
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = 60f,
                center = bocaTarget
            )
        }

        // Cuchara
        Image(
            painter = painterResource(
                if (cucharaLlena) R.drawable.cucharallena else R.drawable.cucharavacia
            ),
            contentDescription = "Cuchara",
            modifier = Modifier
                .size(80.dp)
                .offset {
                    IntOffset(
                        x = (cucharaPos.x - with(density) { 40.dp.toPx() }).roundToInt(),
                        y = (cucharaPos.y - with(density) { 40.dp.toPx() }).roundToInt()
                    )
                }
        )

        // Texto e instrucciones
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (completado) "¡Qué rico! 😋✨" else "¡Lleva la cuchara a la boca!",
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
                color = Color(0xFFFF9800),
                trackColor = Color.White.copy(alpha = 0.3f)
            )
        }

        // Emoji boca en la zona objetivo
        if (!completado) {
            Text(
                text = "👄",
                fontSize = 32.sp,
                modifier = Modifier.offset {
                    IntOffset(
                        x = (bocaTarget.x - with(density) { 20.dp.toPx() }).roundToInt(),
                        y = (bocaTarget.y - with(density) { 20.dp.toPx() }).roundToInt()
                    )
                }
            )
        }

        TextButton(
            onClick = onCancelar,
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) {
            Text(
                "Cancelar",
                color = Color.White,
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(color = Color.Black.copy(alpha = 0.5f), blurRadius = 4f)
                )
            )
        }
    }
}