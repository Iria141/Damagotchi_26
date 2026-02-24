package com.example.damagotchi_26

import com.example.damagotchi_26.viewmodel.TransicionViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.damagotchi_26.data.PetPrefs
import com.example.damagotchi_26.navigation.AppNav
import com.example.damagotchi_26.viewmodel.PetViewModel
import com.example.damagotchi_26.navigation.RoomsPagerScreen
import com.example.damagotchi_26.viewmodel.PetViewModelFactory
import com.example.damagotchi_26.viewmodel.TransicionViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            //prefs: 1 sola instancia
            val petPrefs = remember { PetPrefs(applicationContext) }

            //PetViewModel
            val petFactory = remember { PetViewModelFactory(applicationContext) }
            val petViewModel: PetViewModel = viewModel(factory = petFactory)

            // TransicionViewModel (con factory)
            val transFactory = remember { TransicionViewModelFactory(petPrefs) }
            val transicionViewModel: TransicionViewModel = viewModel(factory = transFactory)


            LaunchedEffect(Unit) {
                petViewModel.iniciarTick()
                petViewModel.iniciarTiempo()
                transicionViewModel.iniciarCicloLuz()
            }

            MaterialTheme {
                Surface {
                    AppNav(
                        transicionViewModel = transicionViewModel,
                        petViewModel = petViewModel,
                        momentoDia = transicionViewModel.momentoDia
                    )
                }
            }
        }
    }
}
