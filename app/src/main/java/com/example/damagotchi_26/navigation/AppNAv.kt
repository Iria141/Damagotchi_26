package com.example.damagotchi_26.navigation

import CommunityScreen
import android.util.Log
import android.widget.Toast
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
import com.example.damagotchi_26.ui.theme.Welcome
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.damagotchi_26.data.AnuncioSeguimiento
import com.example.damagotchi_26.data.UserPreferences
import com.example.damagotchi_26.data.getUserProfile
import com.example.damagotchi_26.repository.saveAnuncioSeguimiento
import com.example.damagotchi_26.ui.Community.CreatePostScreen
import com.example.damagotchi_26.ui.Community.PostDetailScreen
import com.example.damagotchi_26.ui.Menu.Menu
import com.example.damagotchi_26.ui.MiEmbarazo.CrearAnuncioAdmin
import com.example.damagotchi_26.ui.MiEmbarazo.SeguimientoScreem


@Composable
fun AppNav(
    startDestination: String,
    transicionViewModel: TransicionViewModel,
    petViewModel: PetViewModel,
    momentoDia: StateFlow<MomentoDia>,
    onRememberMeChanged: suspend (Boolean) -> Unit
) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    var userProfile by remember { mutableStateOf<com.example.damagotchi_26.data.UserProfile?>(null) }
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val welcomeSeen by userPreferences.welcomeSeenFlow.collectAsState(initial = false)

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

                            val uid = FirebaseAuth.getInstance().currentUser?.uid

                            /*  Toast.makeText(
                                context,
                                "UID login: $uid",
                                Toast.LENGTH_LONG
                            ).show()

                            Log.d("LOGIN_UID", "UID login: $uid") */

                            if (uid != null) {
                                getUserProfile(uid) { profile, profileError ->
                                    if (profile != null) {
                                        userProfile = profile

                                        val destino = if (!welcomeSeen) {
                                            Route.Welcome.path
                                        } else {
                                            Route.Menu.path
                                        }

                                        navController.navigate(destino) {
                                            popUpTo(Route.Login.path) { inclusive = true }
                                            launchSingleTop =
                                                true //Evita abrir pantallas duplicadas

                                        }


                                    } else {
                                        Toast.makeText(
                                            context,
                                            profileError ?: "Error cargando perfil",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "No se pudo obtener el usuario",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        } else {
                            Toast.makeText(
                                context,
                                error ?: "Error al iniciar sesión",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                },
                onGoRegister = {
                    navController.navigate(Route.Register.path)
                    { launchSingleTop = true }
                },
                onForgotPassword = {
                    navController.navigate(Route.ResetPassword.path)
                    { launchSingleTop = true }
                }
            )
        }

        composable(Route.Rooms.path) {
            RoomsPagerScreen(
                transicionViewModel = transicionViewModel,
                petViewModel = petViewModel,
                momentoDia = momentoDia,
                nombre = userProfile?.nombre ?: "Usuario",
                rol = userProfile?.rol ?: "Otro"
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

        composable(Route.Welcome.path) {
            Welcome(
                nombre = userProfile?.nombre ?: "Usuario",
                rol = userProfile?.rol ?: "Otro",
                onStart = {
                    scope.launch { userPreferences.setWelcomeSeen(true) }

                    navController.navigate(Route.Menu.path) {
                        popUpTo(Route.Welcome.path) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Route.ResetPassword.path) {
            ResetPassword(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Route.PasswordResetDone.path) {
            androidx.compose.material3.Text("Se ha enviado un email para restablecer tu contraseña")
        }


        composable(Route.Menu.path) {
            Menu(
                rol = userProfile?.rol ?: "Otro",
                nombre = userProfile?.nombre ?: "Usuario",
                onPlayClick = {
                    navController.navigate(Route.Rooms.path)
                    { launchSingleTop = true } //Evita abrir pantallas duplicadas
                },
                onCommunityClick = {
                    navController.navigate(Route.Community.path)
                    { launchSingleTop = true }
                },
                onSeguimientoClick = {
                    navController.navigate(Route.SeguimientoScreem.path)
                    { launchSingleTop = true }

                }
            )
        }

        composable(Route.Community.path) {
            CommunityScreen(
                onCreatePostClick = { navController.navigate(Route.CreatePost.path) },
                onPostClick = { postId ->
                    navController.navigate("detalle_post/$postId")
                },
                onBack = { navController.popBackStack() }

            )
        }

        composable(Route.CreatePost.path) {
            CreatePostScreen(
                isAdmin = (userProfile?.rol ?: "").lowercase() == "admin",
                onPublishClick = { titulo, contenido, tipo ->
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }

            )
        }

        composable(Route.PostDetail.path) { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: ""

            PostDetailScreen(
                postId = postId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Route.SeguimientoScreem.path) {
            SeguimientoScreem(
                rol = userProfile?.rol ?: "Otro",
                semanaReal = userProfile?.semanaGestacion?.toIntOrNull() ?: 1,
                nombre = userProfile?.nombre ?: "Usuario",
                onBack = { navController.popBackStack() },
                onAddAnuncioClick = {
                    navController.navigate(Route.CrearAnuncioAdmin.path)
                }
            )
        }

        composable(Route.CrearAnuncioAdmin.path) {
            CrearAnuncioAdmin(
                onBack = { navController.popBackStack() },
                onPublishClick = { titulo, semana, categoria, contenido, fuente, url ->
                    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

                    val semanaInt = semana.toIntOrNull()

                    if (semanaInt == null) {
                        Toast.makeText(
                            context,
                            "La semana de gestación debe ser un número",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        val anuncio = AnuncioSeguimiento(
                            titulo = titulo,
                            semanaGestacion = semanaInt,
                            categoria = categoria,
                            contenido = contenido,
                            fuente = fuente,
                            urlFuente = url,
                            autorUid = uid
                        )

                        saveAnuncioSeguimiento(anuncio) { ok, error ->
                            if (ok) {
                                Toast.makeText(
                                    context,
                                    "Anuncio guardado correctamente",
                                    Toast.LENGTH_LONG
                                ).show()

                                navController.popBackStack()
                            } else {
                                Toast.makeText(
                                    context,
                                    error ?: "Error al guardar anuncio",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            )
        }



    }
}
