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
import kotlin.math.sqrt
import kotlin.random.Random

data class GotaCrema(
    val x: Float,
    val y: Float,
    val radio: Float,
    var alpha: Float = 1f
)

@Composable
fun CremaOverlay(
    onCompletado: () -> Unit,
    onCancelar: () -> Unit
) {
    val density = LocalDensity.current
    var progreso by remember { mutableStateOf(0f) }
    var cremaPos by remember { mutableStateOf(Offset(400f, 600f)) }
    val gotas = remember { mutableStateListOf<GotaCrema>() }
    var completado by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(50)
            gotas.replaceAll { it.copy(alpha = it.alpha - 0.03f) }
            gotas.removeAll { it.alpha <= 0f }
        }
    }

    LaunchedEffect(completado) {
        if (completado) {
            delay(1000)
            onCompletado()
        }
    }

    Box(modifier = Modifier.fillMaxSize().zIndex(50f)) {

        // Área de arrastre transparente
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset -> cremaPos = offset },
                        onDrag = { change, dragAmount ->
                            if (!completado) {
                                cremaPos = change.position

                                // Rastro rosado brillante
                                repeat(3) {
                                    gotas.add(
                                        GotaCrema(
                                            x = cremaPos.x + Random.nextFloat() * 40f - 20f,
                                            y = cremaPos.y + Random.nextFloat() * 40f - 20f,
                                            radio = Random.nextFloat() * 14f + 5f
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

        // Rastro de crema rosada
        Canvas(modifier = Modifier.fillMaxSize()) {
            gotas.forEach { gota ->
                drawCircle(
                    color = Color(0xFFFFC0CB).copy(alpha = gota.alpha * 0.7f),
                    radius = gota.radio,
                    center = Offset(gota.x, gota.y)
                )
                drawCircle(
                    color = Color(0xFFFFE4E8).copy(alpha = gota.alpha * 0.5f),
                    radius = gota.radio * 0.6f,
                    center = Offset(gota.x - gota.radio * 0.2f, gota.y - gota.radio * 0.2f)
                )
            }
        }

        // Crema siguiendo el dedo exacto
        Image(
            painter = painterResource(R.drawable.crema),
            contentDescription = "Crema",
            modifier = Modifier
                .size(70.dp)
                .offset {
                    IntOffset(
                        x = (cremaPos.x - with(density) { -20.dp.toPx() }).roundToInt(),
                        y = (cremaPos.y - with(density) { -80.dp.toPx() }).roundToInt()
                    )
                }
        )

        // Barra de progreso
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (completado) "¡Piel hidratada! 🧴✨" else "¡Aplica la crema!",
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
                color = Color(0xFFFF85A1),
                trackColor = Color.White.copy(alpha = 0.3f)
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