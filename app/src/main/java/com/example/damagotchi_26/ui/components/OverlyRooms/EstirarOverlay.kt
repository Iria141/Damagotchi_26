package com.example.damagotchi_26.ui.rooms

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.damagotchi_26.R
import kotlinx.coroutines.delay

@Composable
fun EstirarOverlay(
    onCompletado: () -> Unit
) {
    val secuencia = listOf(
        R.drawable.costado_izq_abierto,
        R.drawable.estira_izq,
        R.drawable.costado_dcha_abierto,
        R.drawable.estira_dcha
    )

    val objetivo = 8
    var frameActual by remember { mutableStateOf(0) }
    var pulsaciones by remember { mutableStateOf(0) }
    var completado by remember { mutableStateOf(false) }
    var haciaDerecha by remember { mutableStateOf(true) }

    val progreso = pulsaciones / objetivo.toFloat()

    LaunchedEffect(completado) {
        if (completado) {
            delay(600)
            onCompletado()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f))
            .zIndex(50f)
    ) {
        // Personaje con animación de entrada
        AnimatedContent(
            targetState = frameActual,
            transitionSpec = {
                if (haciaDerecha) {
                    slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                } else {
                    slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
                }
            },
            label = "pose",
            modifier = Modifier.align(Alignment.Center)
        ) { frame ->
            Image(
                painter = painterResource(secuencia[frame]),
                contentDescription = "Estiramiento",
                modifier = Modifier.size(400.dp)
            )
        }

        // Progreso y texto arriba
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (completado) "¡Genial! 🤸✨"
                else "¡Estira! ($pulsaciones/$objetivo)",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(color = Color.Black.copy(alpha = 0.5f), blurRadius = 4f)
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { progreso.coerceAtMost(1f) },
                modifier = Modifier.fillMaxWidth().height(10.dp),
                color = Color(0xFFAB47BC),
                trackColor = Color.White.copy(alpha = 0.3f)
            )
        }

        // Botones izq y dcha abajo
        if (!completado) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 100.dp)
                    .padding(horizontal = 48.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón izquierda
                Button(
                    onClick = {
                        haciaDerecha = false
                        frameActual = (frameActual - 1 + secuencia.size) % secuencia.size
                        pulsaciones++
                        if (pulsaciones >= objetivo) completado = true
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(72.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.85f)
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "←",
                        fontSize = 28.sp,
                        color = Color(0xFFAB47BC),
                        fontWeight = FontWeight.Bold
                    )
                }

                // Contador central
                Box(
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${objetivo - pulsaciones}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        style = LocalTextStyle.current.copy(
                            shadow = Shadow(color = Color.Black.copy(alpha = 0.4f), blurRadius = 4f)
                        )
                    )
                }

                // Botón derecha
                Button(
                    onClick = {
                        haciaDerecha = true
                        frameActual = (frameActual + 1) % secuencia.size
                        pulsaciones++
                        if (pulsaciones >= objetivo) completado = true
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(72.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.85f)
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "→",
                        fontSize = 28.sp,
                        color = Color(0xFFAB47BC),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}