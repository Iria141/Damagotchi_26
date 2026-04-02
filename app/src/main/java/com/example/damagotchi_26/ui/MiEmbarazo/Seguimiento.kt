package com.example.damagotchi_26.ui.MiEmbarazo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.damagotchi_26.data.CategoriaInformativa
import com.example.damagotchi_26.ui.Color.Color.PurpleBlueText
import com.example.damagotchi_26.ui.components.AuthBackground
import com.example.damagotchi_26.ui.components.SoftDisclaimer
import com.example.damagotchi_26.ui.components.SweetSectionCard
import com.example.damagotchi_26.ui.components.WeekHighlightCard
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.example.damagotchi_26.data.PublicacionInformativa
import com.example.damagotchi_26.navigation.Route
import com.example.damagotchi_26.repository.PublicacionesInformativasRepository
import com.example.damagotchi_26.ui.components.BackTextButton
import com.example.damagotchi_26.ui.components.PrimaryAuthButton

@Composable
fun SeguimientoScreem(
    rol: String,
    semanaReal: Int,
    nombre: String = "",
    onBack: () -> Unit = {},
    onAddAnuncioClick: () -> Unit = {},
    ) {
    val esMama = rol.lowercase() == "mamá" || rol.lowercase() == "mama"
    val titulo = if (esMama) "Mi embarazo" else "Seguimiento"

    val trimestre = obtenerTrimestre(semanaReal)
    val mensajeSemana = obtenerMensajeSemana(semanaReal)

    val repositorio = remember { PublicacionesInformativasRepository() }
    var publicaciones by remember { mutableStateOf<List<PublicacionInformativa>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var errorCarga by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        repositorio.obtenerPublicaciones(
            onResultado = { lista ->
                println("DATOS FIREBASE: ${lista.size}")
                publicaciones = lista
                cargando = false
            },
            onError = { error ->
                errorCarga = error.message
                cargando = false
            }
        )
    }

    val publicacionesFiltradas = publicaciones
        .filter { pub ->
            semanaReal in pub.semanaInicio..pub.semanaFin &&
                    (pub.rolDestino.isEmpty() || rol in pub.rolDestino)
        }
        .sortedByDescending { it.destacada }


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
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                if (rol.lowercase() == "admin") {
                    PrimaryAuthButton(
                        text = "Añadir anuncio",
                        onClick = onAddAnuncioClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "🌸 $titulo 🌸",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurpleBlueText
                )

                Spacer(modifier = Modifier.height(20.dp))

                WeekHighlightCard(
                    title = "Tu momento actual",
                    weekText = "Semana $semanaReal",
                    subtitle = trimestre
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (cargando) {
                    Text(
                        text = "Cargando publicación...",
                        color = PurpleBlueText
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (errorCarga != null) {
                    Text(
                        text = "Error al cargar publicaciones: $errorCarga",
                        color = PurpleBlueText
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                publicacionesFiltradas.forEach { pub ->

                    SweetSectionCard(
                        title = pub.titulo,
                        emoji = obtenerEmoji(pub.categoria),
                        text = pub.contenido
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                }
                if (!cargando && publicacionesFiltradas.isEmpty()) {
                    SweetSectionCard(
                        title = "Aún no hay contenido",
                        emoji = "✨",
                        text = "Todavía no hay publicaciones disponibles para esta semana."
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                }

                SoftDisclaimer(
                    modifier = Modifier
                        .padding(bottom = 80.dp),
                    text = "La información mostrada es orientativa y no sustituye la valoración de profesionales de la salud."

                )

            }
        }
    }
}

    fun obtenerTrimestre(semana: Int): String {
        return when (semana) {
            in 1..13 -> "Primer trimestre"
            in 14..27 -> "Segundo trimestre"
            in 28..40 -> "Tercer trimestre"
            else -> "Semana fuera de rango"
        }
    }

    fun obtenerMensajeSemana(semana: Int): String {
        return when (semana) {
            in 1..13 -> "Es una etapa de muchos cambios iniciales. Puede ser útil priorizar el descanso, resolver dudas frecuentes y mantener un seguimiento cercano."
            in 14..27 -> "Suele ser una fase algo más estable. Es un buen momento para reforzar hábitos saludables y seguir aprendiendo sobre esta etapa."
            in 28..40 -> "En esta etapa final puede apetecer más calma, preparación y acompañamiento. También puede ser útil revisar información práctica y señales a tener en cuenta."
            else -> "No hay información disponible para esta semana."
        }
    }

    fun obtenerEmoji(categoria: String): String {
        return when (categoria) {
            CategoriaInformativa.HIDRATACION -> "💧"
            CategoriaInformativa.MASCOTAS -> "🐾"
            CategoriaInformativa.ALIMENTACION -> "🥦"
            CategoriaInformativa.SALUD -> "🩺"
            CategoriaInformativa.RECURSOS -> "📚"
            else -> "✨"
        }
    }




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SeguimientoScreenPreview() {
    MaterialTheme {
        SeguimientoScreem(
            rol = "mamá",
            semanaReal = 28,
            nombre = "Iria"
        )
    }
}