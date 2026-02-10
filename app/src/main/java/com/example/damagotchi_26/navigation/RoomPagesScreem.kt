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
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomsPagerScreen(petViewModel: PetViewModel, momentoDia: StateFlow<MomentoDia>) {
    val petEstado by petViewModel.pet.collectAsState()

    val rooms = listOf("Salón", "Cocina", "Dormitorio", "Baño", "Consulta Médica", "Parque")
    val pagerState = rememberPagerState(pageCount = { rooms.size })

    Scaffold(
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

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> LivingRoom(
                        pet = petEstado,
                        beber = { petViewModel.hidratar() },
                        verTV = { petViewModel.verTV() },
                        leer = { petViewModel.leer() }
                    )

                    1 -> Kitchen(
                        pet = petEstado,
                        comer = { petViewModel.alimentar() },
                        beber = { petViewModel.hidratar() }
                    )


                    2 -> BedRoom(
                        pet = petEstado,
                        dormir = { petViewModel.dormir() }
                    )

                    3 -> BathRoom (
                        pet = petEstado,
                        limpieza = { petViewModel.higiene()}
                    )

                    4 -> Park(
                        pet = petEstado,
                        caminar = {petViewModel.caminar()},
                        yoga = {petViewModel.yoga()}
                    )

                    5 -> Clinic(
                        pet = petEstado
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
