package com.example.damagotchi_26.ui.rooms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.damagotchi_26.R
import com.example.damagotchi_26.domain.Pet
import com.example.damagotchi_26.ui.theme.trimestreDeSemana
import com.example.damagotchi_26.ui.components.IconsPanel
import com.example.damagotchi_26.ui.theme.recordatoriosPorTrimestre
import kotlinx.coroutines.delay
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.damagotchi_26.ui.components.NightOverlay
import com.example.damagotchi_26.viewmodel.TransicionViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Clinic(
    pet: Pet,
    rol: String,
    transicionViewModel: TransicionViewModel
) {
    val momento by transicionViewModel.momentoDia.collectAsState()

    val trimestreActual = trimestreDeSemana(pet.semanaEmbarazo) //devuelve el trimestre, por lo que dice que etapa esta el personaje
    val listaRecordatorios = recordatoriosPorTrimestre(trimestreActual, rol) // devuelve la lista de consejos, que van rotando
    //Hace que los mensajes sean aleatorios segun el trimestre
    var idx by remember(trimestreActual) { mutableIntStateOf(0) } //Indice del recordatorio actual

    LaunchedEffect(trimestreActual, listaRecordatorios.size) {
        idx = 0
        if (listaRecordatorios.isEmpty()) return@LaunchedEffect
        while (true) {
            delay(10000) // cada 10s cambia
            idx = (idx + 1) % listaRecordatorios.size
        }
    }
    val recordatorio = listaRecordatorios.getOrNull(idx) //Recordatorio equivalente al indice actual
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        transicionViewModel.avisos.collect { msg ->
            val result = snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Long
            )
        }
    }

    val diaActual by transicionViewModel.diaActual.collectAsState()
    LaunchedEffect(diaActual, pet.semanaEmbarazo, rol) {
        transicionViewModel.comprobarConsejoDelDia(
            semana = pet.semanaEmbarazo,
            dia = diaActual,
            rol = rol
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Consulta",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 30.sp
                        )

                        Text(
                            text = "Energía: ${pet.energia}%",
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
            // Fondo
            Image(
                painter = painterResource(id = R.drawable.consulta),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            NightOverlay(momento = momento, maxDarkness = 0.55f) //Fondo oscurecido cuando "anochece"


            //Iconos
            IconsPanel(pet = pet)
            Spacer(Modifier.width(12.dp))

            //Mensaje Recordatorio
            if (recordatorio != null) {

                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(16.dp, 50.dp)
                        .zIndex(10f),
                    border = BorderStroke(5.dp, MaterialTheme.colorScheme.outline),
                    shape = RoundedCornerShape(20.dp),
                    tonalElevation = 6.dp,
                    shadowElevation = 8.dp,

                    ) {

                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                        }

                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "🩺 Consejo:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp
                        )

                        Spacer(Modifier.height(10.dp))

                        Text(
                            text = recordatorio.texto,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}


