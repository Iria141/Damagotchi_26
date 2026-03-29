package com.example.damagotchi_26.ui.Menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.damagotchi_26.ui.Color.Color.PurpleBlueText
import com.example.damagotchi_26.ui.components.AuthBackground
import com.example.damagotchi_26.ui.components.MenuOptionCard

@Composable
fun Menu(
    rol: String,
    nombre: String = "",
    onPlayClick: () -> Unit = {},
    onCommunityClick: () -> Unit = {},
    onSeguimientoClick: () -> Unit = {}
) {
    val trackingTitle = if (rol.lowercase() == "mamá" || rol.lowercase() == "mama") {
        "Mi embarazo"
    } else {
        "Seguimiento"
    }

    val trackingSubtitle = if (trackingTitle == "Mi embarazo") {
        "Información útil según tu semana real"
    } else {
        "Información y apoyo según la etapa actual"
    }

    val welcomeText = if (nombre.isNotBlank()) {
        "Bienvenida, $nombre"
    } else {
        "Bienvenida"
    }

    AuthBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = welcomeText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp) ,
                textAlign = TextAlign.Center,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = PurpleBlueText,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Elige una sección para continuar",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = PurpleBlueText
            )

            Spacer(modifier = Modifier.height(28.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MenuOptionCard(
                    title = "Modo juego",
                    subtitle = "Cuida de tu Damagotchi y avanza en el juego",
                    emoji = "🎮",
                    onClick = onPlayClick
                )

                Spacer(modifier = Modifier.height(6.dp))

                MenuOptionCard(
                    title = "Comunidad",
                    subtitle = "Comparte dudas, opiniones y publicaciones",
                    emoji = "💬",
                    onClick = onCommunityClick
                )

                Spacer(modifier = Modifier.height(6.dp))

                MenuOptionCard(
                    title = trackingTitle,
                    subtitle = trackingSubtitle,
                    emoji = "🤰",
                    onClick = onSeguimientoClick
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenMamaPreview() {
    MaterialTheme {
        Menu(
            rol = "mamá",
            nombre = "Iria"
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenAcompanantePreview() {
    MaterialTheme {
        Menu(
            rol = "acompañante",
            nombre = "Alex"
        )
    }
}