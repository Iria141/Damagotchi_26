package com.example.damagotchi_26.ui.rooms

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
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
import com.example.damagotchi_26.ui.Color.Color.PinkBg
import com.example.damagotchi_26.ui.components.IconsPanel
import com.example.damagotchi_26.ui.components.NightOverlay
import com.example.damagotchi_26.ui.components.OverlyRooms.PianoOverlay
import com.example.damagotchi_26.ui.components.OverlyRooms.PintarOverlay
import com.example.damagotchi_26.ui.components.ActionButton
import com.example.damagotchi_26.viewmodel.TransicionViewModel

private enum class AccionSalon { NINGUNA, PIANO, PINTAR }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LivingRoom(
    pet: Pet,
    tocarPiano: () -> Unit,
    pintar: () -> Unit,
    sonidosActivados: Boolean = true
) {
    val vm: TransicionViewModel = viewModel()
    val momento by vm.momentoDia.collectAsState()
    val dia by vm.diaActual.collectAsState()
    val semana by vm.semanaActual.collectAsState()
    var accionActiva by remember { mutableStateOf(AccionSalon.NINGUNA) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PinkBg
                ),
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Salón",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 30.sp
                        )
                        Text(
                            text = "Descanso: ${pet.descanso}%  |  Actividad: ${pet.actividad}%  |  Energía: ${pet.energia}%",
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
                painter = painterResource(id = R.drawable.salon),
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
                    .padding(20.dp)
                    .zIndex(10f),
                horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionButton(
                    image = R.drawable.piano,
                    text = "Piano",
                    onClick = {
                        if (accionActiva == AccionSalon.NINGUNA)
                            accionActiva = AccionSalon.PIANO
                    }
                )

                ActionButton(
                    image = R.drawable.paleta_colores,
                    text = "Pintar",
                    onClick = {
                        if (accionActiva == AccionSalon.NINGUNA)
                            accionActiva = AccionSalon.PINTAR
                    }
                )
            }

            // Overlay Piano
            if (accionActiva == AccionSalon.PIANO) {
                PianoOverlay(
                    onCompletado = {
                        tocarPiano()
                        accionActiva = AccionSalon.NINGUNA
                    },
                    onCancelar = { accionActiva = AccionSalon.NINGUNA }
                )
            }


            // Overlay Pintar
            if (accionActiva == AccionSalon.PINTAR) {
                PintarOverlay(
                    onCompletado = {
                        pintar()
                        accionActiva = AccionSalon.NINGUNA
                    },
                    onCancelar = { accionActiva = AccionSalon.NINGUNA }
                )
            }
        }
    }
}