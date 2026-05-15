package com.example.damagotchi_26.ui.Menu

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.damagotchi_26.R
import com.example.damagotchi_26.domain.Pet
import com.example.damagotchi_26.ui.Color.Color.PurpleBlueText
import com.example.damagotchi_26.ui.Color.Color.PurpleBtn
import com.example.damagotchi_26.ui.components.AuthBackground
import com.example.damagotchi_26.ui.components.WeekHighlightCard
import com.example.damagotchi_26.ui.components.calcularDiaYSemana
import com.example.damagotchi_26.ui.components.drawablePersonaje
import com.example.damagotchi_26.ui.components.trimestresDePersonaje

@Composable
fun Menu(
    rol: String,
    nombre: String = "",
    pet: Pet? = null,
    fechaUltimaRegla: Long,
    onPlayClick: () -> Unit = {},
    onCommunityClick: () -> Unit = {},
    onSeguimientoClick: () -> Unit = {},
    onConfiguracionClick: () -> Unit = {},
    onAyudaClick: () -> Unit = {},
    onCerrarSesionClick: () -> Unit = {}
) {
    val trackingTitle = if (rol.lowercase() == "mamá" || rol.lowercase() == "mama") {
        "Mi embarazo"
    } else {
        "Seguimiento"
    }

    var menuExpandido by remember { mutableStateOf(false) }

    // Animación de pulso para el borde del círculo
    val infiniteTransition = rememberInfiniteTransition(label = "pulso")
    val borderAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "border_alpha"
    )

    val personajeDrawable = if (pet != null) {
        drawablePersonaje(pet, mirandoDerecha = true)
    } else {
        R.drawable.estado_inicial
    }

    val trimestre = pet?.let { trimestresDePersonaje(it.semanaEmbarazo) } ?: 1
    val trimestreTexto = when (trimestre) {
        1 -> "Primer trimestre"
        2 -> "Segundo trimestre"
        3 -> "Tercer trimestre"
        else -> "Recta final"
    }
    val (dia, semanaReal) = calcularDiaYSemana(fechaUltimaRegla)


    AuthBackground {
        Box(modifier = Modifier.fillMaxSize()) {

            // Menu tres puntos
            Box(modifier = Modifier.align(Alignment.TopEnd).padding(top = 65.dp, end = 8.dp)) {
                IconButton(onClick = { menuExpandido = true }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Mas opciones",
                        tint = PurpleBlueText
                    )
                }
                DropdownMenu(
                    expanded = menuExpandido,
                    onDismissRequest = { menuExpandido = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("⚙️ Configuracion") },
                        onClick = { menuExpandido = false; onConfiguracionClick() }
                    )
                    DropdownMenuItem(
                        text = { Text("❓ Ayuda") },
                        onClick = { menuExpandido = false; onAyudaClick() }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text("Cerrar sesion", color = Color(0xFFB00020)) },
                        onClick = { menuExpandido = false; onCerrarSesionClick() }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 52.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
            //Cabecera unificada
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Hola $nombre" ,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = PurpleBlueText
                        )
                        if (pet != null) {
                            Text(
                                text = "Semana $semanaReal",
                                fontSize = 15.sp,
                                color = PurpleBlueText.copy(alpha = 0.65f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


                // ── Card principal — Modo juego ──
                Card(
                    onClick = onPlayClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(270.dp),
                    shape = RoundedCornerShape(32.dp),
                    elevation = CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(containerColor = PurpleBtn)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(170.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(
                                    BorderStroke(4.dp, Color.White.copy(alpha = borderAlpha)),
                                    CircleShape
                                )
                        ) {
                            Image(
                                painter = painterResource(personajeDrawable),
                                contentDescription = "Personaje",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(160.dp)
                                    .padding(8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Modo juego",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Explora las estancias y cuida de tu peronaje",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.85f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }


                // ── Botones secundarios ──
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MenuSecondaryButton(
                        text = trackingTitle,
                        emoji = "🤰",
                        subtitle = "Etapas y consejos",
                        onClick = onSeguimientoClick,
                        modifier = Modifier.weight(1f)
                    )
                    MenuSecondaryButton(
                        text = "Comunidad",
                        emoji = "💬",
                        subtitle = "Comparte y pregunta",
                        onClick = onCommunityClick,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun MenuSecondaryButton(
    text: String,
    emoji: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.88f)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = emoji, fontSize = 26.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = PurpleBlueText,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = PurpleBlueText.copy(alpha = 0.65f),
                textAlign = TextAlign.Center,
                lineHeight = 13.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MenuPreview() {
    MaterialTheme {
        Menu(rol = "mama", nombre = "Iria", pet = Pet(), fechaUltimaRegla = 1735689600000L)
    }
}