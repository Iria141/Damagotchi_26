package com.example.damagotchi_26

import com.example.damagotchi_26.viewmodel.TransicionViewModel
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.damagotchi_26.data.PetPrefs
import com.example.damagotchi_26.navigation.AppNav
import com.example.damagotchi_26.repository.SeedCommunity
import com.example.damagotchi_26.viewmodel.PetViewModel
import com.example.damagotchi_26.viewmodel.PetViewModelFactory
import com.example.damagotchi_26.viewmodel.TransicionViewModelFactory
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.example.damagotchi_26.data.UserPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {

    private val solicitarPermisoNotificacion = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                solicitarPermisoNotificacion.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid != null) {
                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(uid)
                    .update("fcmToken", token)
            }
        }

        SeedCommunity.sembrar()

        setContent {
            val petPrefs = remember { PetPrefs(applicationContext) }
            val userPrefs = remember { UserPreferences(applicationContext) }
            val rememberMe by userPrefs.rememberMeFlow.collectAsState(initial = false)

            val start = if (rememberMe) {
                com.example.damagotchi_26.navigation.Route.Menu.path
            } else {
                com.example.damagotchi_26.navigation.Route.Login.path
            }

            val petFactory = remember { PetViewModelFactory(applicationContext) }
            val petViewModel: PetViewModel = viewModel(factory = petFactory)

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
                        startDestination        = start,
                        transicionViewModel     = transicionViewModel,
                        petViewModel            = petViewModel,
                        momentoDia              = transicionViewModel.momentoDia,
                        onRememberMeChanged     = { value ->
                            userPrefs.setRememberMe(value)
                        }
                    )
                }
            }
        }
    }
}