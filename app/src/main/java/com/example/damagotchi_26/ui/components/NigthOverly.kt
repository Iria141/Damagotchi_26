package com.example.damagotchi_26.ui.components


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.damagotchi_26.domain.MomentoDia

@Composable
fun NightOverlay(
    momento: MomentoDia,
    maxDarkness: Float = 0.55f, // ajusta a tu gusto (0.35–0.70 suele ir bien)
) {
    val targetAlpha = if (momento == MomentoDia.NOCHE) maxDarkness else 0f
    val alpha by animateFloatAsState(targetValue = targetAlpha, label = "night_alpha")

    if (alpha > 0f) {
        // Capa oscura encima
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = alpha))
        )
    }
}
