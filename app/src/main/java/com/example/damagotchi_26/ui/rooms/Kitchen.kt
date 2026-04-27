package com.example.damagotchi_26.ui.rooms

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.damagotchi_26.R
import com.example.damagotchi_26.domain.Pet
import com.example.damagotchi_26.ui.components.IconsPanel
import com.example.damagotchi_26.ui.components.NightOverlay
import com.example.damagotchi_26.ui.components.OverlyRooms.ComerOverlay
import com.example.damagotchi_26.ui.components.OverlyRooms.BeberOverlay
import com.example.damagotchi_26.ui.components.OverlyRooms.PreparacionOverlay
import com.example.damagotchi_26.ui.theme.ActionButton
import com.example.damagotchi_26.viewmodel.TransicionViewModel

private enum class AccionCocina { NINGUNA, COMER, BEBER, INGREDIENTES }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Kitchen(
    pet: Pet,
    comer: () -> Unit,
    beber: () -> Unit,
    prepararComida: () -> Unit
) {
    val vm: TransicionViewModel = viewModel()
    val momento by vm.momentoDia.collectAsState()
    var accionActiva by remember { mutableStateOf(AccionCocina.NINGUNA) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Cocina",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 30.sp
                        )
                        Text(
                            text = "Hambre: ${pet.hambre}%  |  Sed: ${pet.sed}%  |  Acgtividad: ${pet.actividad}%  |  Energía: ${pet.energia}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.cocina),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            NightOverlay(momento = momento, maxDarkness = 0.55f)
            IconsPanel(pet = pet)

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .zIndex(10f),
                horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionButton(
                    image = R.drawable.salteado,
                    text = "Comer",
                    onClick = {
                        if (accionActiva == AccionCocina.NINGUNA)
                            accionActiva = AccionCocina.COMER
                    }
                )
                ActionButton(
                    image = R.drawable.vaso,
                    text = "Beber",
                    onClick = {
                        if (accionActiva == AccionCocina.NINGUNA)
                            accionActiva = AccionCocina.BEBER
                    }
                )
                ActionButton(
                    image = R.drawable.cocinar,
                    text = "Cocinar",
                    onClick = {
                        if (accionActiva == AccionCocina.NINGUNA)
                            accionActiva = AccionCocina.INGREDIENTES
                    }
                )
            }

            // Overlay Comer
            if (accionActiva == AccionCocina.COMER) {
                ComerOverlay(
                    onCompletado = {
                        comer()
                        accionActiva = AccionCocina.NINGUNA
                    },
                    onCancelar = { accionActiva = AccionCocina.NINGUNA }
                )
            }

            // Overlay Beber
            if (accionActiva == AccionCocina.BEBER) {
                BeberOverlay(
                    onCompletado = {
                        beber()
                        accionActiva = AccionCocina.NINGUNA
                    },
                    onCancelar = { accionActiva = AccionCocina.NINGUNA }
                )
            }

            // Overlay Ingredientes
            if (accionActiva == AccionCocina.INGREDIENTES) {
                PreparacionOverlay(
                    onCompletado = {
                        prepararComida()
                        accionActiva = AccionCocina.NINGUNA
                    },
                    onCancelar = { accionActiva = AccionCocina.NINGUNA }
                )
            }
        }
    }
}