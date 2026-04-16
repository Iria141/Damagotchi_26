package com.example.damagotchi_26.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.damagotchi_26.navigation.Route
import com.example.damagotchi_26.ui.components.AuthBackground
import com.example.damagotchi_26.ui.components.AuthCard
import com.example.damagotchi_26.ui.components.PrimaryAuthButton

@Composable
fun Welcome(
    nombre: String,
    rol: String,
    onStart: () -> Unit
) {
    val titulo = when (rol) {
        "Madre" -> "¡Bienvenida a Damagotchi, $nombre!"
        "Padre" -> "¡Bienvenido a Damagotchi, $nombre!"
        else -> "¡Bienvenida/o a Damagotchi, $nombre!"
    }

    val subtitulo = when (rol) {
        "Madre" -> "Tu compañera de aventura en el embarazo"
        "Padre" -> "Tu espacio para acompañar, apoyar y cuidar"
        else -> "Tu espacio de apoyo, aprendizaje y acompañamiento"
    }

    val descripcion = when (rol) {
        "Madre" ->
            "En Damagotchi acompañarás a tu personaje en su emocionante camino durante la maternidad.\n" +
                    "✨ Supervisarás sus necesidades para mantener un buen estado de salud.\n" +
                    "✨ Descubrirás consejos y mensajes motivadores.\n" +
                    "✨ Personalizarás el entorno y vivirás una experiencia llena de color y significado."

        "Padre" ->
            "En Damagotchi podrás acompañar, apoyar y cuidar en esta aventura.\n" +
                    "✨ Ayudarás a supervisar necesidades importantes.\n" +
                    "✨ Encontrarás consejos útiles y mensajes de apoyo.\n" +
                    "✨ Participarás en una experiencia cercana, motivadora y especial."

        else ->
            "En Damagotchi encontrarás un espacio de apoyo, aprendizaje y acompañamiento.\n" +
                    "✨ Podrás supervisar necesidades importantes.\n" +
                    "✨ Descubrirás consejos y mensajes motivadores.\n" +
                    "✨ Vivirás una experiencia cuidada, visual y significativa."
    }

    AuthBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = titulo,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = subtitulo,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = descripcion,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    PrimaryAuthButton(
                        text = "¡Comenzamos!",
                        onClick = onStart
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomeScreenPreview() {
    MaterialTheme {
        Surface {
            Welcome(
                nombre = "Laura",
                rol = "Madre",
                onStart = { }
            )
        }
    }
}