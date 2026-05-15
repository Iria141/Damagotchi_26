package com.example.damagotchi_26.ui.components.OverlyRooms

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.damagotchi_26.R

@Composable
fun SiestaOverlay(
    alphaAnimado: Float
) {
    if (alphaAnimado > 0f) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFD080).copy(alpha = alphaAnimado))
                .zIndex(20f),
            contentAlignment = Alignment.Center
        ) {
            if (alphaAnimado > 0.3f) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "☀️", fontSize = 44.sp)
                    Text(
                        text = "Echando una siesta...",
                        fontSize = 20.sp,
                        color = Color(0xFF5A3A00),
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                    Text(text = "😴", fontSize = 44.sp)
                }
            }
        }
    }
}

@Composable
fun DormirOverlay(
    alphaAnimado: Float,
    sonidosActivados: Boolean = true
) {
    val context = LocalContext.current

    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.respirar).apply {
            isLooping = true
        }
    }

    LaunchedEffect(alphaAnimado) {
        if (alphaAnimado > 0.3f && !mediaPlayer.isPlaying && sonidosActivados) {
            mediaPlayer.start()
        } else if (alphaAnimado <= 0f && mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            mediaPlayer.seekTo(0)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            if (mediaPlayer.isPlaying) mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    if (alphaAnimado > 0f) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0A0A2A).copy(alpha = alphaAnimado))
                .zIndex(20f),
            contentAlignment = Alignment.Center
        ) {
            if (alphaAnimado > 0.5f) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = "🌙 ✨ ⭐ ✨ 🌙", fontSize = 32.sp)
                    Text(
                        text = "Durmiendo...",
                        fontSize = 22.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        fontWeight = FontWeight.Light
                    )
                    Text(text = "💤", fontSize = 48.sp)
                }
            }
        }
    }
}