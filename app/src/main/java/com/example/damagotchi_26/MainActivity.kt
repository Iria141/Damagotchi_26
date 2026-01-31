package com.example.damagotchi_26

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.damagotchi_26.data.PetPrefs
import com.example.damagotchi_26.viewmodel.PetViewModel
import com.example.damagotchi_26.navigation.RoomsPagerScreen
import com.example.damagotchi_26.viewmodel.PetViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val factory = PetViewModelFactory(applicationContext)
            val petViewModel: PetViewModel = viewModel(factory = factory)

            LaunchedEffect(Unit) {
                petViewModel.iniciarTick()
            }

            RoomsPagerScreen(petViewModel = petViewModel)
        }
    }
}

