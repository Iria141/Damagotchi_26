package com.example.damagotchi_26.ui.components.OverlyRooms

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay

@Composable
fun BanoOverlay(
    onCompletado: () -> Unit
) {
    var alpha by remember { mutableStateOf(0f) }

    val alphaAnimado by animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(durationMillis = 1000),
        label = "alpha_bano"
    )

    LaunchedEffect(Unit) {
        alpha = 0.80f        // se llena de agua azul
        delay(3000)          // dura 3 segundos
        alpha = 0f           // se vacía
        delay(1000)
        onCompletado()
    }

    if (alphaAnimado > 0f) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0055AA).copy(alpha = alphaAnimado))
                .zIndex(50f),
            contentAlignment = Alignment.Center
        ) {
            if (alphaAnimado > 0.4f) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "🛁", fontSize = 52.sp)
                    Text(
                        text = "Dándose un baño...",
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        style = LocalTextStyle.current.copy(
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.3f),
                                blurRadius = 4f
                            )
                        )
                    )
                    Text(text = "🐟 💧 🐟", fontSize = 24.sp)
                }
            }
        }
    }
}