package com.example.damagotchi_26.ui.login

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.damagotchi_26.ui.components.AuthBackground
import com.example.damagotchi_26.ui.components.AuthCard
import com.example.damagotchi_26.ui.components.AuthTextField
import com.example.damagotchi_26.ui.components.BackTextButton
import com.google.firebase.auth.FirebaseAuth


@Composable
fun ResetPassword(
    onBack: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Correo enviado") },
            text = { Text("Puede tardar unos minutos en llegar.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        onBack()
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }

    fun resetPassword(email: String, onResult: (Boolean, String?) -> Unit) {
        val auth = FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    onResult(true, null)
                else
                    onResult(false, task.exception?.message)
            }
    }

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                BackTextButton(onClick = onBack) // 👈 centrado abajo
            }
        }
    ) { paddingValues ->
        AuthBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AuthCard {
                    AuthTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email",
                        isError = emailError,
                        errorMessage = "Email no válido"
                    )
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        emailError = false
                        when {
                            email.isBlank() -> emailError = true
                            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> emailError = true
                            else -> {
                                resetPassword(email) { ok, _ ->
                                    if (ok) showDialog = true
                                }
                            }
                        }
                    },
                    enabled = email.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 100.dp)
                ) {
                    Text("Enviar email")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ResetPasswordPreview() {
    MaterialTheme {
        Surface {
            ResetPassword(
                onBack = { }
            )
        }
    }
}