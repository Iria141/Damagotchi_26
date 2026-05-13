package com.example.damagotchi_26.ui.MiEmbarazo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.damagotchi_26.ui.Color.Color.PurpleBlueText
import com.example.damagotchi_26.ui.components.AuthBackground
import com.example.damagotchi_26.ui.components.BackTextButton
import com.example.damagotchi_26.ui.components.SweetSectionCard

@Composable
fun DetalleAnuncioScreen(
    titulo: String,
    categoria: String,
    contenido: String,
    semanaGestacion: Int,
    fuente: String,
    imagenUrl: String,
    onBack: () -> Unit
) {
var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }


    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp),
                contentAlignment = Alignment.Center
            ) {
                BackTextButton(onClick = onBack)
            }
        }
    ) { paddingValues ->
        AuthBackground {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(500)) +
                        slideInVertically(
                            animationSpec = tween(500),
                            initialOffsetY = { it / 5 }
                        )
            ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp, vertical = 20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = titulo,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurpleBlueText,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                if (imagenUrl.isNotBlank()) {
                    AsyncImage(
                        model = imagenUrl,
                        contentDescription = titulo,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp)
                            .clip(RoundedCornerShape(24.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(18.dp))
                }
                SweetSectionCard(
                    title = "Categoría",
                    emoji = obtenerEmoji(categoria),
                    text = formatearCategoria(categoria),
                    backgroundColor = obtenerColorCategoria(categoria)
                )

                Spacer(modifier = Modifier.height(14.dp))

                SweetSectionCard(
                    title = "Semana recomendada",
                    emoji = "📅",
                    text = "Semana $semanaGestacion",
                    backgroundColor = obtenerColorCategoria(categoria)
                )

                Spacer(modifier = Modifier.height(14.dp))

                SweetSectionCard(
                    title = "Información detallada",
                    emoji = "📝",
                    text = contenido,
                    backgroundColor = obtenerColorCategoria(categoria)
                )

                if (fuente.isNotBlank()) {
                    Spacer(modifier = Modifier.height(14.dp))

                    SweetSectionCard(
                        title = "Fuente",
                        emoji = "🔎",
                        text = fuente,
                        backgroundColor = obtenerColorCategoria(categoria)
                    )
                }
            }
        }
    }
    }
}

private fun formatearCategoria(categoria: String): String {
    return when (categoria.lowercase()) {
        "hidratacion" -> "Hidratación"
        "mascotas" -> "Mascotas"
        "alimentacion" -> "Alimentación"
        "salud" -> "Salud"
        "recursos" -> "Recursos"
        "descanso" -> "Descanso"
        "bienestar_emocional" -> "Bienestar emocional"
        "desarrollo_bebe" -> "Desarrollo del bebé"
        else -> categoria.replaceFirstChar { it.uppercase() }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetalleAnuncioScreenPreview() {
    MaterialTheme {
        DetalleAnuncioScreen(
            titulo = "Hidratación en el embarazo",
            categoria = "hidratacion",
            contenido = "Beber agua con frecuencia ayuda a mantener un buen bienestar general durante el embarazo.",
            semanaGestacion = 24,
            fuente = "OMS",
            imagenUrl = "https://images.unsplash.com/photo-1502741338009-cac2772e18bc",
            onBack = {}
        )
    }
}