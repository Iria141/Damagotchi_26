package com.example.damagotchi_26

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import com.example.damagotchi_26.viewmodel.PetViewModel
import com.example.damagotchi_26.ui.rooms.RoomsPagerScreen
import com.example.damagotchi_26.viewmodel.PetViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = androidx.navigation.compose.rememberNavController()
            val petViewModel: PetViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(factory = PetViewModelFactory(this))

            LaunchedEffect(Unit) {
                petViewModel.iniciarTick()
            }
            RoomsPagerScreen(petViewModel)
        }
    }
}
