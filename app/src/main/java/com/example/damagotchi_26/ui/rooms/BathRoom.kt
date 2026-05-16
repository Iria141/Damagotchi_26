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
import com.example.damagotchi_26.ui.Color.Color.PinkBg
import com.example.damagotchi_26.ui.components.IconsPanel
import com.example.damagotchi_26.ui.components.NightOverlay
import com.example.damagotchi_26.ui.components.OverlyRooms.BanoOverlay
import com.example.damagotchi_26.ui.components.ActionButton
import com.example.damagotchi_26.viewmodel.TransicionViewModel

private enum class AccionBano { NINGUNA, DUCHA, BANO, DIENTES, CREMA }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BathRoom(
    pet: Pet,
    bano: () -> Unit,
    ducharse: () -> Unit,
    lavarDientes: () -> Unit,
    cuidarPiel: () -> Unit,
    sonidosActivados: Boolean = true
) {
    val vm: TransicionViewModel = viewModel()
    val momento by vm.momentoDia.collectAsState()

    var accionActiva by remember { mutableStateOf(AccionBano.NINGUNA) }

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
                            text = "Cuarto de Baño",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 30.sp
                        )
                        Text(
                            text = "Higiene: ${pet.limpieza}%  |  Energía: ${pet.energia}%",
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
                painter = painterResource(id = R.drawable.bano),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            NightOverlay(momento = momento, maxDarkness = 0.55f)
            IconsPanel(pet = pet)

            // Botones
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp)
                    .zIndex(10f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionButton(
                    image = R.drawable.esponja,
                    text = "Ducha",
                    onClick = {
                        if (accionActiva == AccionBano.NINGUNA)
                            accionActiva = AccionBano.DUCHA
                    }
                )
                ActionButton(
                    image = R.drawable.banera,
                    text = "Baño",
                    onClick = {
                        if (accionActiva == AccionBano.NINGUNA)
                            accionActiva = AccionBano.BANO
                    }
                )
                ActionButton(
                    image = R.drawable.cepillo,
                    text = "Cepillar",
                    onClick = {
                        if (accionActiva == AccionBano.NINGUNA)
                            accionActiva = AccionBano.DIENTES
                    }
                )
                ActionButton(
                    image = R.drawable.crema,
                    text = "Crema",
                    onClick = {
                        if (accionActiva == AccionBano.NINGUNA)
                            accionActiva = AccionBano.CREMA
                    }
                )
            }

            // Overlay Ducha
            if (accionActiva == AccionBano.DUCHA) {
                DuchaOverlay(
                    onCompletado = {
                        ducharse()
                        accionActiva = AccionBano.NINGUNA
                    },
                    onCancelar = {
                        accionActiva = AccionBano.NINGUNA
                    }
                )
            }

            // Overlay Baño
            if (accionActiva == AccionBano.BANO) {
                BanoOverlay (
                    onCompletado = {
                        bano()
                        accionActiva = AccionBano.NINGUNA
                    }
                )
            }

            // Overlay Dientes
            if (accionActiva == AccionBano.DIENTES) {
                DientesOverlay(
                    onCompletado = {
                        lavarDientes()
                        accionActiva = AccionBano.NINGUNA
                    },
                    onCancelar = { accionActiva = AccionBano.NINGUNA }
                )
            }

            // Overlay Crema
            if (accionActiva == AccionBano.CREMA) {
                CremaOverlay(
                    onCompletado = {
                        cuidarPiel()
                        accionActiva = AccionBano.NINGUNA
                    },
                    onCancelar = { accionActiva = AccionBano.NINGUNA }
                )
            }
        }
    }
}