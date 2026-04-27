package com.example.damagotchi_26.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.damagotchi_26.domain.MomentoDia
import com.example.damagotchi_26.ui.rooms.BathRoom
import com.example.damagotchi_26.ui.rooms.BedRoom
import com.example.damagotchi_26.ui.rooms.Clinic
import com.example.damagotchi_26.ui.rooms.Kitchen
import com.example.damagotchi_26.ui.rooms.LivingRoom
import com.example.damagotchi_26.ui.rooms.Park
import com.example.damagotchi_26.viewmodel.PetViewModel
import com.example.damagotchi_26.viewmodel.TransicionViewModel
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomsPagerScreen(
    transicionViewModel: TransicionViewModel,
    petViewModel: PetViewModel,
    momentoDia: StateFlow<MomentoDia>,
    nombre: String,
    rol: String
) {
    val petEstado by petViewModel.pet.collectAsState(initial = null)

    if (petEstado == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator() // Muestra un cargando mientras el DataStore despierta
        }
        return // Detiene la ejecución aquí hasta que petEstado tenga datos
    }

    val pet = petEstado!!
    val rooms = listOf("Salón", "Cocina", "Dormitorio", "Baño", "Parque", "Consulta Médica")
    val pagerState = rememberPagerState(pageCount = { rooms.size })


    LaunchedEffect(pet.semanaEmbarazo) {
        transicionViewModel.comprobarAvisoTrimestre(
            pet.semanaEmbarazo,
            rol = rol)
    }

    val snackbarHostState = remember { SnackbarHostState() }




    Scaffold(
        snackbarHost = {
            Box(
                modifier = Modifier.fillMaxSize()
                    .padding(bottom = 10.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                SnackbarHost(
                    hostState = snackbarHostState
                ) { data ->
                    Surface(
                        tonalElevation = 4.dp,
                        shadowElevation = 8.dp,
                        shape = MaterialTheme.shapes.extraLarge,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                    ) {
                        Text(
                            text = data.visuals.message,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(top = 50.dp)
                .fillMaxSize()
        ) {
            // Indicador simple (puntitos)
            RoomDots(
                total = rooms.size,
                current = pagerState.currentPage,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> LivingRoom(
                        pet = pet,
                        beber = { petViewModel.hidratar() },
                        verTV = { petViewModel.verTV() },
                        leer = { petViewModel.leer() }
                    )

                    1 -> Kitchen(
                        pet = pet,
                        comer = { petViewModel.alimentar() },
                        beber = { petViewModel.hidratar() },
                        prepararComida = { petViewModel.prepararComida() }
                    )


                    2 -> BedRoom(
                        pet = pet,
                        dormir = { petViewModel.dormir() },
                        dormirProgresivo = { petViewModel.dormirProgresivo() },
                        siesta = { petViewModel.siesta() },
                        meditar = { petViewModel.meditar() }
                    )

                    3 -> BathRoom(
                        pet = pet,
                        limpieza = { petViewModel.higiene() },
                        irAlBano = { petViewModel.irAlBano() },
                        ducharse = { petViewModel.ducharse() },
                        lavarDientes = { petViewModel.lavarDientes() },
                        cuidarPiel = { petViewModel.cuidarPiel() }
                    )

                    4 -> Park(
                        pet = pet,
                        caminar = { petViewModel.caminar() },
                        yoga = { petViewModel.yoga() },
                        estirar = { petViewModel.estirar() }
                    )

                    5 -> Clinic(
                        pet = pet,
                        rol = rol,
                        transicionViewModel = transicionViewModel
                    )

                }
            }
        }
    }
}

@Composable
private fun RoomDots(total: Int, current: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(total) { index ->
            val selected = index == current
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(if (selected) 10.dp else 8.dp)
            ) {
                // usando Surface para puntitos
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.fillMaxSize()
                ) {}
            }
        }
    }
}