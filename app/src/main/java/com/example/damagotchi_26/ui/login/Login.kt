package com.example.damagotchi_26.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.damagotchi_26.ui.components.AuthBackground
import com.example.damagotchi_26.ui.components.AuthCard
import com.example.damagotchi_26.ui.components.textStyle
import com.example.damagotchi_26.ui.Color.Color.PurpleBlueText
import com.example.damagotchi_26.ui.components.AuthTextField
import com.example.damagotchi_26.viewmodel.TransicionViewModel
import com.google.firebase.auth.FirebaseAuth



// FUNCIÓN QUE REALIZA EL LOGIN EN FIREBASE
// email -> correo del usuario
// password -> contraseña
// onResult -> devuelve si el login fue correcto o no

fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {

    // Obtiene la instancia de autenticación de Firebase
    val auth = FirebaseAuth.getInstance()
    // Intenta iniciar sesión con email y contraseña
    auth.signInWithEmailAndPassword(email, password)

        // Escucha cuando termina el proceso
        .addOnCompleteListener { task ->
            // Si el login fue correcto
            if (task.isSuccessful)
                onResult(true, null)
            // Si hubo error devuelve el mensaje
            else
                onResult(false, task.exception?.message)
        }
}


// COMPOSABLE QUE REPRESENTA LA PANTALLA DE LOGIN
@Composable
fun Login(

    transicionViewModel: TransicionViewModel? = null,

    onLogin: (user: String, pass: String, remember: Boolean) -> Unit,
    onGoRegister: () -> Unit,
    onForgotPassword: () -> Unit
) {

    // ESTADO DEL CAMPO EMAIL
    var user by remember { mutableStateOf("") }

    // ESTADO DEL CAMPO CONTRASEÑA
    var pass by remember { mutableStateOf("") }

    // ESTADO DEL CHECKBOX "RECUÉRDAME"
    var rememberMe by remember { mutableStateOf(false) }


    // FONDO GENERAL DE LA PANTALLA (TU COMPONENTE PERSONALIZADO)
    AuthBackground {

        // COLUMNA PRINCIPAL
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {

            // ESPACIO SUPERIOR
            Spacer(Modifier.height(16.dp))


            AuthCard {
                // CAMPO EMAIL
                AuthTextField(
                    value = user,
                    onValueChange = { user = it },
                    label = "Email"
                )

                // CAMPO CONTRASEÑA
                AuthTextField(
                    value = pass,
                    onValueChange = { pass = it },
                    label = "Contraseña",
                    isPassword = true
                )

                // FILA PARA CHECKBOX Y RECUPERAR CONTRASEÑA
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp),

                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    // CHECKBOX RECUÉRDAME
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Checkbox(
                            modifier = Modifier.size(22.dp),
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it }
                        )

                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            style = textStyle,
                            text = "Recuérdame"
                        )
                    }


                    // BOTÓN RESTABLECER CONTRASEÑA
                    TextButton(
                        onClick = onForgotPassword,
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = PurpleBlueText
                        )
                    ) {
                        Text(
                            style = textStyle,
                            text = "Restablecer Contraseña"
                        )
                    }
                }


                // BOTÓN INICIAR SESIÓN
                Button(
                    onClick = {
                        // VALIDAMOS QUE LOS CAMPOS NO ESTÉN VACÍOS
                        if (user.isNotBlank() && pass.isNotBlank()) {

                            // LLAMAMOS A LA FUNCIÓN DE LOGIN
                            onLogin(user, pass, rememberMe)
                        }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 90.dp)
                ) {
                    Text("Iniciar Sesión")
                }


                // BOTÓN PARA IR A REGISTRO
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    TextButton(
                        onClick = onGoRegister,
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.textButtonColors(contentColor = PurpleBlueText)
                    ) {

                        Text(
                            text = "No tengo cuenta. DAR DE ALTA",
                            style = textStyle
                        )
                    }
                }
            }
        }
    }
}


// PREVISUALIZACIÓN DE LA PANTALLA LOGIN
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginPreview() {
    MaterialTheme {
        Surface {
            Login(
                onLogin = { _, _, _ -> },
                onGoRegister = { },
                onForgotPassword = { }
            )
        }
    }
}
