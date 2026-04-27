package com.example.damagotchi_26.ui.components.OverlyRooms

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.graphicsLayer
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
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun BeberOverlay(
    onCompletado: () -> Unit,
    onCancelar: () -> Unit
) {
    val density = LocalDensity.current
    var angulo by remember { mutableStateOf(0f) }
    var progreso by remember { mutableStateOf(0f) }
    var completado by remember { mutableStateOf(false) }
    val gotas = remember { mutableStateListOf<Offset>() }

    val anguloAnimado by animateFloatAsState(
        targetValue = angulo,
        animationSpec = tween(80),
        label = "angulo_vaso"
    )

    progreso = (abs(anguloAnimado) / 90f).coerceAtMost(1f)

    LaunchedEffect(progreso) {
        if (progreso >= 1f && !completado) {
            completado = true
        }
    }

    // Añadir gotitas mientras se inclina
    LaunchedEffect(anguloAnimado) {
        if (abs(anguloAnimado) > 25f && !completado) {
            repeat(2) {
                gotas.add(
                    Offset(
                        x = 400f + Random.nextFloat() * 60f - 30f,
                        y = 500f + Random.nextFloat() * 40f
                    )
                )
            }
        }
    }

    // Desvanece gotitas
    LaunchedEffect(Unit) {
        while (true) {
            delay(100)
            if (gotas.size > 20) gotas.removeAt(0)
        }
    }

    LaunchedEffect(completado) {
        if (completado) {
            delay(800)
            onCompletado()
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.35f))
            .zIndex(50f)
    ) {
        val anchoPx = with(density) { maxWidth.toPx() }
        val altoPx = with(density) { maxHeight.toPx() }

        // Área de arrastre
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures { _, dragAmount ->
                        if (!completado) {
                            angulo = (angulo - dragAmount.x * 0.4f).coerceIn(-100f, 0f)
                        }
                    }
                }
        )

        // Gotitas cayendo
        Canvas(modifier = Modifier.fillMaxSize()) {
            gotas.forEachIndexed { i, gota ->
                drawCircle(
                    color = Color(0xFF4FC3F7).copy(alpha = 0.7f - i * 0.03f),
                    radius = 8f + (i % 3) * 4f,
                    center = Offset(
                        anchoPx / 2f - 30f + gota.x % 60f,
                        altoPx * 0.4f + gota.y % 100f + i * 8f
                    )
                )
            }
        }

        // Vaso rotando — cambia imagen
        val drawable = when {
            abs(anguloAnimado) < 30f -> R.drawable.vasolleno
            abs(anguloAnimado) < 60f -> R.drawable.vasomedio
            else -> R.drawable.vasovacio
        }

        Image(
            painter = painterResource(drawable),
            contentDescription = "Vaso de agua",
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.Center)
                .graphicsLayer {
                    rotationZ = anguloAnimado
                    transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.5f, 1f)
                }
        )

        // Instrucciones
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (completado) "¡Hidratada! 💧✨"
                else "¡Inclina el vaso hacia la izquierda!",
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
                color = Color(0xFF4FC3F7),
                trackColor = Color.White.copy(alpha = 0.3f)
            )
        }

        // Flecha indicadora
        if (!completado && abs(anguloAnimado) < 20f) {
            Text(
                text = "arrastra → →",
                fontSize = 18.sp,
                color = Color.White.copy(alpha = 0.7f),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = 100.dp),
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(color = Color.Black.copy(alpha = 0.5f), blurRadius = 4f)
                )
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