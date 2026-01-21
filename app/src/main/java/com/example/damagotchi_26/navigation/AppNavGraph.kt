package com.example.damagotchi_26.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.damagotchi_26.ui.rooms.LivingRoom
import com.example.damagotchi_26.viewmodel.PetViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.damagotchi_26.ui.rooms.Kitchen
import com.example.damagotchi_26.ui.rooms.GameRoom

@Composable
fun AppNavGraph(
    navController: NavHostController,
    petViewModel: PetViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            val petEstado by petViewModel.pet.collectAsState()

            LivingRoom ( //Llamada a Salon
                pet = petEstado,
                comer = { navController.navigate(Routes.KITCHEN) },
                jugar = { navController.navigate(Routes.GAMEROOM) }
            )
        }

        composable(Routes.KITCHEN) {
            val petEstado by petViewModel.pet.collectAsState()

            Kitchen ( // Es la llamada a la pantalla  Kitchrn
                pet = petEstado,
                comer = { petViewModel.alimentar() },
                beber = { petViewModel.alimentar() }
            )
        }

        composable(Routes.GAMEROOM) {
            val petEstado by petViewModel.pet.collectAsState()

            GameRoom ( // llamada a la pantalla juego
                pet = petEstado,
                onJugar = { petViewModel.jugar() }
            )
        }
    }
}


