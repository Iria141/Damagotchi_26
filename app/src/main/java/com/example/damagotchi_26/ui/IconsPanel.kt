package com.example.damagotchi_26.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.damagotchi_26.R


private enum class Medidor { ENERGIA, HAMBRE, SED, HIGIENE, ACTIVIDAD, SUEÑO }

@Composable
fun IconsPanel(
    pet: Pet,
    modifier: Modifier = Modifier
) {
    var medidorActivo by remember { mutableStateOf<Medidor?>(null) }

    Box(
        modifier= Modifier
            .fillMaxWidth()

    ) {
        Image(
            painter = painterResource(R.drawable.estado_inicial),
            contentDescription = "Personaje",
            modifier = Modifier
                .width(300.dp)
                .height(400.dp)
                .align(Alignment.Center)
                .padding(start = 80.dp)
        )

        Column(
            modifier = modifier.width(90.dp)
                .padding(top = 100.dp)
                .padding(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
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
                .size(25.dp)
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
