package com.example.damagotchi_26.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.damagotchi_26.domain.MomentoDia
import com.example.damagotchi_26.ui.login.Login
import com.example.damagotchi_26.viewmodel.PetViewModel
import com.example.damagotchi_26.viewmodel.TransicionViewModel
import kotlinx.coroutines.flow.StateFlow


sealed class Route(val path: String) {
    data object Login : Route("login")
    data object Register : Route("register")
    data object AccountCreated : Route("account_created")
    data object ResetPassword : Route("reset_password")
    data object PasswordResetDone : Route("password_reset_done")
    data object Rooms : Route("rooms")
}

@Composable
fun AppNav(
    transicionViewModel: TransicionViewModel,
    petViewModel: PetViewModel,
    momentoDia: StateFlow<MomentoDia>
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Login.path
    ) {
        composable(Route.Login.path) {
            Login(
                onLogin = { _, _ ->
                    navController.navigate(Route.Rooms.path) {
                        popUpTo(Route.Login.path) { inclusive = true }
                    }
                },
                onGoRegister = { navController.navigate(Route.Register.path) },
                onForgotPassword = { navController.navigate(Route.ResetPassword.path) }
            )
        }

        composable(Route.Rooms.path) {
            RoomsPagerScreen(
                transicionViewModel = transicionViewModel,
                petViewModel = petViewModel,
                momentoDia = momentoDia
            )
        }

        composable(Route.Register.path) { /* ... */ }
        composable(Route.AccountCreated.path) { /* ... */ }
        composable(Route.ResetPassword.path) { /* ... */ }
        composable(Route.PasswordResetDone.path) { /* ... */ }
    }
}
