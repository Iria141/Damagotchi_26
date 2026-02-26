package com.example.damagotchi_26.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.damagotchi_26.ui.Color.Color.BorderGray
import com.example.damagotchi_26.ui.Color.Color.CardGray
import com.example.damagotchi_26.ui.components.AuthBackground
import com.example.damagotchi_26.ui.components.AuthCard
import com.example.damagotchi_26.ui.components.PrimaryAuthButton
import com.example.damagotchi_26.ui.components.textStyle
import com.example.damagotchi_26.ui.Color.Color.PurpleBlueText
import com.example.damagotchi_26.viewmodel.TransicionViewModel
import com.example.damagotchi_26.viewmodel.TransicionViewModelFactory


@Composable
fun Login(
    onLogin: (user: String, pass: String, remember: Boolean) -> Unit,
    onGoRegister: () -> Unit,
    onForgotPassword: () -> Unit
) {
    // Estado del campo usuario
    var user by remember { mutableStateOf("") }

    // Estado del campo contraseña
    var pass by remember { mutableStateOf("") }

    var rememberMe by remember { mutableStateOf(false) }

    val context = LocalContext.current
    // Creamos la instancia de PetPrefs usando el context
    val petPrefs = remember { com.example.damagotchi_26.data.PetPrefs(context) }
    // Se la pasamos a la Factory (que ahora espera PetPrefs por la Opción B)
    val transicionViewModel: TransicionViewModel = viewModel(
        factory = TransicionViewModelFactory(petPrefs)
    )

    // Fondo general de la pantalla (rosa)
    AuthBackground {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {

            // Espacio superior
            Spacer(Modifier.height(16.dp))

            // Tarjeta que agrupa los campos de texto
            AuthCard {
                // CAMPO USUARIO
                OutlinedTextField(
                    value = user,
                    onValueChange = { user = it }, // Actualiza el estado
                    label = { Text("Usuario") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        // Fondo gris fijo
                        focusedContainerColor = CardGray,
                        unfocusedContainerColor = CardGray,
                        disabledContainerColor = CardGray,

                        // Borde gris (no cambia al enfocar)
                        focusedBorderColor = BorderGray,
                        unfocusedBorderColor = BorderGray,
                        disabledBorderColor = BorderGray,

                        // Color de la etiqueta
                        focusedLabelColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray,

                        // Color del cursor
                        cursorColor = Color.DarkGray
                    )
                )

                // CAMPO CONTRASEÑA
                OutlinedTextField(
                    value = pass,
                    onValueChange = { pass = it },
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(), // Oculta el texto
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardGray,
                        unfocusedContainerColor = CardGray,
                        disabledContainerColor = CardGray,

                        focusedBorderColor = BorderGray,
                        unfocusedBorderColor = BorderGray,
                        disabledBorderColor = BorderGray,

                        focusedLabelColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray,

                        cursorColor = Color.DarkGray
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
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

                TextButton(
                    onClick = onGoRegister,
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

            PrimaryAuthButton(
                text = "Iniciar Sesión",
                onClick = { onLogin(user, pass, rememberMe) }, // Llama al login con los datos
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 100.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(
                    onClick = onForgotPassword,
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