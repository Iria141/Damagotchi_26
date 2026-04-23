package com.example.damagotchi_26.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.damagotchi_26.domain.Pet
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.example.damagotchi_26.R


private enum class Medidor { ENERGIA, HAMBRE, SED, HIGIENE, ACTIVIDAD, SUEÑO }

// Determina el trimestre según la semana
fun trimestresDePersonaje(semana: Int): Int = when (semana) {
    in 1..12 -> 1
    in 13..20 -> 2
    in 21..30 -> 3
    else -> 4  // 31-40
}

// Determina el estado emocional según los medidores
fun estadoEmocional(pet: Pet): String {
    val criticos = listOf(pet.energia, pet.hambre, pet.sed, pet.limpieza, pet.actividad, pet.descanso)
        .count { it < 30 }
    val bajos = listOf(pet.energia, pet.hambre, pet.sed, pet.limpieza, pet.actividad, pet.descanso)
        .count { it < 50 }
    return when {
        criticos >= 2 -> "triste"
        bajos >= 3 -> "triste"
        listOf(pet.energia, pet.hambre, pet.sed, pet.limpieza, pet.actividad, pet.descanso)
            .all { it >= 70 } -> "superfeliz"
        else -> "feliz"
    }
}

// Selecciona el drawable correcto
fun drawablePersonaje(pet: Pet, mirandoDerecha: Boolean): Int {
    val trimestre = trimestresDePersonaje(pet.semanaEmbarazo)
    val estado = estadoEmocional(pet)
    val dir = if (mirandoDerecha) "dcha" else "izq"

    // T4 solo tiene estado_inicial (sin variantes de dirección/estado)
    if (trimestre == 4) return R.drawable.estado_inicial

    return when ("${estado}_${dir}_t${trimestre}") {
        "feliz_dcha_t1" -> R.drawable.feliz_dcha_t1
        "feliz_dcha_t2" -> R.drawable.feliz_dcha_t2
        "feliz_dcha_t3" -> R.drawable.feliz_dcha_t3
        "feliz_izq_t1"  -> R.drawable.feliz_izq_t1
        "feliz_izq_t2"  -> R.drawable.feliz_izq_t2
        "feliz_izq_t3"  -> R.drawable.feliz_izq_t3
        "superfeliz_dcha_t1" -> R.drawable.superfeliz_dcha_t1
        "superfeliz_dcha_t2" -> R.drawable.superfeliz_dcha_t2
        "superfeliz_dcha_t3" -> R.drawable.superfeliz_dcha_t3
        "superfeliz_izq_t1"  -> R.drawable.superfeliz_izq_t1
        "superfeliz_izq_t2"  -> R.drawable.superfeliz_izq_t2
        "superfeliz_izq_t3"  -> R.drawable.superfeliz_izq_t3
        "triste_dcha_t1" -> R.drawable.triste_dcha_t1
        "triste_dcha_t2" -> R.drawable.triste_dcha_t2
        "triste_dcha_t3" -> R.drawable.triste_dcha_t3
        "triste_izq_t1"  -> R.drawable.triste_izq_t1
        "triste_izq_t2"  -> R.drawable.triste_izq_t2
        "triste_izq_t3"  -> R.drawable.triste_izq_t3
        else -> R.drawable.estado_inicial
    }
}

@Composable
fun IconsPanel(
    pet: Pet,
    modifier: Modifier = Modifier
) {
    var medidorActivo by remember { mutableStateOf<Medidor?>(null) }

    // Animación de movimiento lateral — va y vuelve en 4 segundos
    val infiniteTransition = rememberInfiniteTransition(label = "personaje")
    val offsetX by infiniteTransition.animateFloat(
        initialValue = -60f,
        targetValue = 60f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "movimiento"
    )

    // Determina si mira a la derecha según la dirección del movimiento
    val mirandoDerecha = offsetX > 0f
    val drawable = drawablePersonaje(pet, mirandoDerecha)

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(drawable),
            contentDescription = "Personaje",
            modifier = Modifier
                .width(400.dp)
                .height(500.dp)
                .align(Alignment.Center)
                .padding(start = 80.dp)
                .graphicsLayer { translationX = offsetX }
        )

        Column(
            modifier = modifier
                .width(90.dp)
                .padding(vertical = 190.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            MedidorIcono(
                icono = R.drawable.trueno,
                valor = pet.energia,
                activo = medidorActivo == Medidor.ENERGIA,
                onToggle = {
                    medidorActivo = if (medidorActivo == Medidor.ENERGIA) null else Medidor.ENERGIA
                }
            )

            MedidorIcono(
                icono = R.drawable.comida,
                valor = pet.hambre,
                activo = medidorActivo == Medidor.HAMBRE,
                onToggle = {
                    medidorActivo = if (medidorActivo == Medidor.HAMBRE) null else Medidor.HAMBRE
                }
            )

            MedidorIcono(
                icono = R.drawable.agua,
                valor = pet.sed,
                activo = medidorActivo == Medidor.SED,
                onToggle = {
                    medidorActivo = if (medidorActivo == Medidor.SED) null else Medidor.SED
                }
            )

            MedidorIcono(
                icono = R.drawable.ducha,
                valor = pet.limpieza,
                activo = medidorActivo == Medidor.HIGIENE,
                onToggle = {
                    medidorActivo = if (medidorActivo == Medidor.HIGIENE) null else Medidor.HIGIENE
                }
            )

            MedidorIcono(
                icono = R.drawable.actividad,
                valor = pet.actividad,
                activo = medidorActivo == Medidor.ACTIVIDAD,
                onToggle = {
                    medidorActivo =
                        if (medidorActivo == Medidor.ACTIVIDAD) null else Medidor.ACTIVIDAD
                }
            )

            MedidorIcono(
                icono = R.drawable.dormir,
                valor = pet.descanso,
                activo = medidorActivo == Medidor.SUEÑO,
                onToggle = {
                    medidorActivo = if (medidorActivo == Medidor.SUEÑO) null else Medidor.SUEÑO
                }
            )
        }
    }
}

@Composable
private fun MedidorIcono(
    icono: Int,
    valor: Int,
    activo: Boolean,
    onToggle: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = icono),
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .clickable { onToggle() }
        )
        AnimatedVisibility(
            visible = activo, // Solo muestra el interior (barra porcentual) si visible = true
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .width(80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("$valor%", style = MaterialTheme.typography.labelMedium)
                LinearProgressIndicator(
                    progress = {valor / 100f},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(999.dp))
                )
            }
        }
    }
}