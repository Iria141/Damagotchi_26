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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.example.damagotchi_26.data.UserPreferences

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val petPrefs = remember { PetPrefs(applicationContext) } //prefs del juego
            val userPrefs = remember { UserPreferences(applicationContext) } //prefs del login
            val rememberMe by userPrefs.rememberMeFlow.collectAsState(initial = false)

            val start = if (rememberMe) {
                com.example.damagotchi_26.navigation.Route.Rooms.path
            } else {
                com.example.damagotchi_26.navigation.Route.Login.path
            }

            val scope = rememberCoroutineScope()


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
                        startDestination = start,
                        transicionViewModel = transicionViewModel,
                        petViewModel = petViewModel,
                        momentoDia = transicionViewModel.momentoDia,
                        onRememberMeChanged = { value ->
                            userPrefs.setRememberMe(value)

                        }
                    )
                }
            }
        }
    }
}

