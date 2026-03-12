package com.example.damagotchi_26.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.damagotchi_26.domain.MomentoDia
import com.example.damagotchi_26.ui.login.Login
import com.example.damagotchi_26.viewmodel.PetViewModel
import com.example.damagotchi_26.viewmodel.TransicionViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.damagotchi_26.ui.login.NewUser
import com.example.damagotchi_26.ui.login.login
import com.example.damagotchi_26.ui.login.register
import com.example.damagotchi_26.ui.login.AccountCreated
import com.example.damagotchi_26.ui.login.ResetPassword
import com.example.damagotchi_26.data.UserProfile
import com.example.damagotchi_26.data.saveUserProfile
import com.google.firebase.auth.FirebaseAuth


sealed class Route(val path: String) {
    data object Login : Route("login")
    data object Register : Route("newUser")
    data object AccountCreated : Route("account_created")
    data object ResetPassword : Route("reset_password")
    data object PasswordResetDone : Route("password_reset_done")
    data object Rooms : Route("rooms")
}

@Composable
fun AppNav(
    startDestination: String,
    transicionViewModel: TransicionViewModel,
    petViewModel: PetViewModel,
    momentoDia: StateFlow<MomentoDia>,
    onRememberMeChanged: suspend (Boolean) -> Unit
) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope ()



    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Route.Login.path) {
            Login(
                transicionViewModel = transicionViewModel,
                onLogin = { user, pass, remember ->
                    login(user, pass) { ok, error ->
                        if (ok) {
                            scope.launch { onRememberMeChanged(remember) }

                            navController.navigate(Route.Rooms.path) {
                                popUpTo(Route.Login.path) { inclusive = true }
                                launchSingleTop = true
                            }
                        } else {
                            println("Login error: ${error ?: "desconocido"}")
                            // Si quieres, aquí luego metemos Snackbar/AlertDialog
                        }
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

        composable(Route.Register.path) {

            NewUser(
                onCreate = { nombre, fecha, rol, semana, sexo, email, pass ->
                    register(email, pass) { ok, error ->
                        if (ok) {
                            val uid = FirebaseAuth.getInstance().currentUser?.uid

                            if (uid != null) {
                                val profile = UserProfile(
                                    nombre = nombre,
                                    fechaNacimiento = fecha,
                                    rol = rol,
                                    semanaGestacion = semana,
                                    sexoBebe = sexo,
                                    email = email
                                )
                                saveUserProfile(uid, profile) { savedOk, saveError ->
                                    if (savedOk) {
                                        navController.navigate(Route.AccountCreated.path) {
                                            popUpTo(Route.Register.path) { inclusive = true }
                                            launchSingleTop = true
                                        }
                                    } else {
                                        println("Error guardando perfil: ${saveError ?: "desconocido"}")
                                    }
                                }
                            } else {
                                println("No se pudo obtener el uid del usuario")
                            }

                        } else {
                            println("Register error: ${error ?: "desconocido"}")
                        }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Route.AccountCreated.path) {
            AccountCreated(
                onGoLogin = {
                    navController.navigate(Route.Login.path) {
                        popUpTo(Route.Login.path) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Route.ResetPassword.path) {
            ResetPassword(
                onBack = { navController.popBackStack() }
            )        }
        composable(Route.PasswordResetDone.path) {
            androidx.compose.material3.Text("Se ha enviado un email para restablecer tu contraseña")
        }
    }
}
