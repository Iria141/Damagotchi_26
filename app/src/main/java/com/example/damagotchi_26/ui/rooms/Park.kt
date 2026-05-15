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
import com.example.damagotchi_26.ui.components.OverlyRooms.CaminarOverlay
import com.example.damagotchi_26.ui.components.OverlyRooms.PelotaOverlay
import com.example.damagotchi_26.ui.theme.ActionButton
import com.example.damagotchi_26.viewmodel.TransicionViewModel

private enum class AccionParque { NINGUNA, CAMINAR, ESTIRAR, PELOTA }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Park(
    pet: Pet,
    caminar: () -> Unit,
    estirar: () -> Unit,
    jugarPelota: () -> Unit = {},
    sonidosActivados: Boolean = true
) {
    val vm: TransicionViewModel = viewModel()
    val momento by vm.momentoDia.collectAsState()
    var accionActiva by remember { mutableStateOf(AccionParque.NINGUNA) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PinkBg
                ),                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Parque",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 30.sp
                        )
                        Text(
                            text = "Actividad: ${pet.actividad}%  |  Energía: ${pet.energia}%",
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
                painter = painterResource(id = R.drawable.parque),
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
                    image = R.drawable.caminar,
                    text = "Caminar",
                    onClick = {
                        if (accionActiva == AccionParque.NINGUNA)
                            accionActiva = AccionParque.CAMINAR
                    }
                )

                ActionButton(
                    image = R.drawable.pilates,
                    text = "Estirar",
                    onClick = {
                        if (accionActiva == AccionParque.NINGUNA)
                            accionActiva = AccionParque.ESTIRAR
                    }
                )

                ActionButton(
                    image = R.drawable.pelota,
                    text = "Pelota",
                    onClick = {
                        if (accionActiva == AccionParque.NINGUNA)
                            accionActiva = AccionParque.PELOTA
                    }
                )
            }

            // Overlay Caminar
            if (accionActiva == AccionParque.CAMINAR) {
                CaminarOverlay(
                    onCompletado = {
                        caminar()
                        accionActiva = AccionParque.NINGUNA
                    }
                )
            }

            // Overlay Estirar
            if (accionActiva == AccionParque.ESTIRAR) {
                EstirarOverlay(
                    onCompletado = {
                        estirar()
                        accionActiva = AccionParque.NINGUNA
                    }
                )
            }

            // Overlay Pelota
            if (accionActiva == AccionParque.PELOTA) {
                PelotaOverlay(
                    onCompletado = {
                        jugarPelota()
                        accionActiva = AccionParque.NINGUNA
                    }
                )
            }
        }
    }
}