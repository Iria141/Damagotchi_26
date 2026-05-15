package com.example.damagotchi_26.ui.components.OverlyRooms

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

data class Ingrediente(
    val id: Int,
    val drawable: Int,
    var x: Float,
    var y: Float,
    val velocidad: Float,
    var atrapado: Boolean = false,
    var fuera: Boolean = false
)

val ingredientesDrawables = listOf(
    R.drawable.platano,
    R.drawable.manzana,
    R.drawable.coliflor,
    R.drawable.calabacin,
    R.drawable.zanahoria,
    R.drawable.berenjena
)

@Composable
fun PreparacionOverlay(
    onCompletado: () -> Unit,
    onCancelar: () -> Unit
) {
    val density = LocalDensity.current

    val ingredientes = remember { mutableStateListOf<Ingrediente>() }
    var atrapados by remember { mutableStateOf(0) }
    var totalLanzados by remember { mutableStateOf(0) }
    var completado by remember { mutableStateOf(false) }
    var anchoPanel by remember { mutableStateOf(1080f) }
    var altoPanel by remember { mutableStateOf(1920f) }

    val objetivo = 20 // aumentado de 10 a 20

    LaunchedEffect(completado) {
        if (completado) {
            delay(800)
            onCompletado()
        }
    }

    // Lanza ingredientes progresivamente más rápido
    LaunchedEffect(Unit) {
        delay(500)
        var velocidadBase = 4f
        var idCounter = 0

        while (atrapados < objetivo && !completado) {
            if (totalLanzados < objetivo + 8) { // extras aumentados
                val x = Random.nextFloat() * (anchoPanel - 120f) + 60f
                val drawable = ingredientesDrawables.random()
                ingredientes.add(
                    Ingrediente(
                        id = idCounter++,
                        drawable = drawable,
                        x = x,
                        y = -100f,
                        velocidad = velocidadBase + Random.nextFloat() * 2f
                    )
                )
                totalLanzados++
                velocidadBase = (velocidadBase + 0.3f).coerceAtMost(14f)
            }
            delay(800L - (velocidadBase * 20).toLong().coerceAtMost(600))
        }
    }

    // Mueve ingredientes hacia abajo
    LaunchedEffect(Unit) {
        while (!completado) {
            delay(16)
            ingredientes.replaceAll { ing ->
                if (!ing.atrapado && !ing.fuera) {
                    val nuevaY = ing.y + ing.velocidad
                    ing.copy(
                        y = nuevaY,
                        fuera = nuevaY > altoPanel + 100f
                    )
                } else ing
            }
            ingredientes.removeAll { it.fuera && !it.atrapado }
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(50f)
    ) {
        val anchoPx = with(density) { maxWidth.toPx() }
        val altoPx = with(density) { maxHeight.toPx() }
        anchoPanel = anchoPx
        altoPanel = altoPx

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { tapOffset ->
                        if (!completado) {
                            val radio = 80f
                            val atrapado = ingredientes.firstOrNull { ing ->
                                !ing.atrapado && !ing.fuera &&
                                        kotlin.math.sqrt(
                                            (ing.x - tapOffset.x) * (ing.x - tapOffset.x) +
                                                    (ing.y - tapOffset.y) * (ing.y - tapOffset.y)
                                        ) < radio
                            }
                            atrapado?.let { ing ->
                                val idx = ingredientes.indexOf(ing)
                                if (idx >= 0) {
                                    ingredientes[idx] = ing.copy(atrapado = true)
                                    atrapados++
                                    if (atrapados >= objetivo) completado = true
                                }
                            }
                        }
                    }
                }
        )

        ingredientes.filter { !it.atrapado && !it.fuera }.forEach { ing ->
            Image(
                painter = painterResource(ing.drawable),
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .offset {
                        IntOffset(
                            x = (ing.x - with(density) { 35.dp.toPx() }).roundToInt(),
                            y = (ing.y - with(density) { 35.dp.toPx() }).roundToInt()
                        )
                    }
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (completado) "¡Menú listo! 🍽️✨"
                else "¡Atrapa los ingredientes! $atrapados/$objetivo",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(color = Color.Black.copy(alpha = 0.5f), blurRadius = 4f)
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { atrapados / objetivo.toFloat() },
                modifier = Modifier.fillMaxWidth().height(10.dp),
                color = Color(0xFF66BB6A),
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