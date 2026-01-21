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
import androidx.compose.ui.res.painterResource
import com.example.damagotchi_26.R


private enum class Medidor { ENERGIA, HAMBRE, SED, HIGIENE, ACTIVIDAD, SUEÑO }

@Composable
fun IconsPanel(
    pet: Pet,
    modifier: Modifier = Modifier
) {
    var medidorActivo by remember { mutableStateOf<Medidor?>(null) }

    Column(
        modifier = modifier.width(90.dp),
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
            icono = R.drawable.actividad,
            valor = pet.actividad,
            activo = medidorActivo == Medidor.ACTIVIDAD,
            onToggle = {
                medidorActivo = if (medidorActivo == Medidor.ACTIVIDAD) null else Medidor.ACTIVIDAD
            }
        )
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
            visible = activo,
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
                    progress = valor / 100f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(999.dp))
                )
            }
        }
    }
}
