package com.example.damagotchi_26.ui.login

import android.util.Log
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.damagotchi_26.R
import com.example.damagotchi_26.ui.Color.Color.PurpleBlueText
import com.example.damagotchi_26.ui.components.AuthBackground
import com.example.damagotchi_26.ui.components.AuthCard
import com.example.damagotchi_26.ui.components.AuthTextField
import com.example.damagotchi_26.ui.components.textStyle
import com.example.damagotchi_26.viewmodel.TransicionViewModel
import com.google.firebase.auth.FirebaseAuth

fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
    val auth = FirebaseAuth.getInstance()

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = FirebaseAuth.getInstance().currentUser
                Log.d("LOGIN", "Login correcto: ${user?.email}")
                onResult(true, null)
            } else {
                val error = task.exception?.message
                Log.e("LOGIN", "Error: $error")
                onResult(false, error)
            }
        }
}

@Composable
fun Login(
    transicionViewModel: TransicionViewModel? = null,
    onLogin: (user: String, pass: String, remember: Boolean) -> Unit,
    onGoRegister: () -> Unit,
    onForgotPassword: () -> Unit
) {
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    val context = LocalContext.current

    AuthBackground {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo y nombre de la app
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Damagotchi",
                modifier = Modifier.size(100.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Damagotchi",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = PurpleBlueText,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Tu compañera de embarazo",
                fontSize = 14.sp,
                color = PurpleBlueText.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            AuthCard {
                AuthTextField(
                    value = user,
                    onValueChange = { user = it },
                    label = "Email"
                )

                AuthTextField(
                    value = pass,
                    onValueChange = { pass = it },
                    label = "Contraseña",
                    isPassword = true
                )

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
                        onClick = onForgotPassword,
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = PurpleBlueText
                        )
                    ) {
                        Text(
                            style = textStyle,
                            text = "Restablecer Contraseña",
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }

                Button(
                    onClick = {
                        if (user.isBlank() || pass.isBlank()) {
                            Toast.makeText(
                                context,
                                "Introduce email y contraseña",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            onLogin(user, pass, rememberMe)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("Iniciar Sesión")
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        onClick = onGoRegister,
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = PurpleBlueText
                        )
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                append("No tengo cuenta. ")
                                withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                                    append("DAR DE ALTA")
                                }
                            },
                            style = textStyle
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginPreview() {
    MaterialTheme {
        Surface {
            Login(
                onLogin = { _, _, _ -> },
                onGoRegister = {},
                onForgotPassword = {}
            )
        }
    }
}