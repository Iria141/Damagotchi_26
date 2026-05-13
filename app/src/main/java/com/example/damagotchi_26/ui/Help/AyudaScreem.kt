package com.example.damagotchi_26.ui.Help

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.damagotchi_26.ui.Color.Color.PurpleBlueText
import com.example.damagotchi_26.ui.Color.Color.PurpleBtn
import com.example.damagotchi_26.ui.components.AuthBackground
import com.example.damagotchi_26.ui.components.BackTextButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AyudaScreen(
    onBack: () -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp),
                contentAlignment = Alignment.Center
            ) {
                BackTextButton(onClick = onBack)
            }
        }
    ) { paddingValues ->
        AuthBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "❓ Ayuda",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurpleBlueText,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                AyudaSeccion(
                    emoji = "🎮",
                    titulo = "¿Cómo funciona Damagotchi?",
                    texto = "Damagotchi es una app de acompañamiento durante el embarazo. Tu objetivo es cuidar a tu personaje a lo largo de las 40 semanas de gestación manteniendo sus medidores en buen estado."
                )

                AyudaSeccion(
                    emoji = "📊",
                    titulo = "Los medidores",
                    texto = "Tu personaje tiene 6 medidores que van bajando con el tiempo: Energía, Hambre, Sed, Higiene, Actividad y Descanso. Si los descuidas demasiado, el personaje se pondrá triste. Si 3 o más bajan del 20% durante 2 días consecutivos, recibirás un aviso. Si vuelve a ocurrir, el embarazo no podrá continuar."
                )

                AyudaSeccion(
                    emoji = "🏠",
                    titulo = "Las habitaciones",
                    texto = "Puedes moverte entre 5 habitaciones deslizando la pantalla. En cada una puedes realizar acciones interactivas para subir los medidores — cocinar y comer en la cocina, ducharte y lavarte los dientes en el baño, descansar en el dormitorio, caminar y estirar en el parque, y tocar el piano o pintar en el salón."
                )

                AyudaSeccion(
                    emoji = "👶",
                    titulo = "El personaje",
                    texto = "El personaje cambia de apariencia según el trimestre del embarazo y su estado emocional. Si está bien cuidado aparecerá feliz o superfeliz. Si está descuidado aparecerá triste."
                )

                AyudaSeccion(
                    emoji = "🏆",
                    titulo = "Evaluación final",
                    texto = "Al llegar a la semana 40 recibirás una evaluación de tu cuidado: Excelente, Bien, Mejorable o Deficiente, según el promedio de tus medidores a lo largo del embarazo."
                )

                AyudaSeccion(
                    emoji = "💬",
                    titulo = "La comunidad",
                    texto = "En la sección Comunidad puedes publicar dudas, experiencias o consejos con otras usuarias. Puedes dar like a las publicaciones que te gusten y comentar en las de otras. También recibirás notificaciones cuando alguien publique o responda."
                )

                AyudaSeccion(
                    emoji = "🤰",
                    titulo = "El tablón de anuncios",
                    texto = "En la sección de Seguimiento o Mi embarazo encontrarás anuncios y consejos adaptados a tu semana de gestación y tu rol. La información se actualiza según el trimestre en el que te encuentres."
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun AyudaSeccion(
    emoji: String,
    titulo: String,
    texto: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = emoji, fontSize = 22.sp)
                Text(
                    text = titulo,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurpleBtn
                )
            }
            Text(
                text = texto,
                fontSize = 14.sp,
                color = PurpleBlueText.copy(alpha = 0.8f),
                lineHeight = 20.sp
            )
        }
    }
}