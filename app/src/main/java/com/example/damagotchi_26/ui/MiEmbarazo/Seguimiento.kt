package com.example.damagotchi_26.ui.MiEmbarazo

import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.damagotchi_26.data.AnuncioSeguimiento
import com.example.damagotchi_26.data.CategoriaInformativa
import com.example.damagotchi_26.repository.FavoritoSeguimientoRepository
import com.example.damagotchi_26.repository.getAnunciosSeguimiento
import com.example.damagotchi_26.ui.Color.Color.PurpleBlueText
import com.example.damagotchi_26.ui.components.AuthBackground
import com.example.damagotchi_26.ui.components.BackTextButton
import com.example.damagotchi_26.ui.components.PrimaryAuthButton
import com.example.damagotchi_26.ui.components.SoftDisclaimer
import com.example.damagotchi_26.ui.components.SweetSectionCard
import com.example.damagotchi_26.ui.components.WeekHighlightCard
import com.example.damagotchi_26.ui.components.calcularDiaYSemana

@Composable
fun SeguimientoScreem(
    rol: String,
    fechaUltimaRegla: Long,
    onBack: () -> Unit = {},
    onAddAnuncioClick: () -> Unit = {},
    onAnuncioClick: (
        titulo: String,
        categoria: String,
        contenido: String,
        semanaGestacion: Int,
        fuente: String,
        imagenUrl: String,

    ) -> Unit = { _, _, _, _, _, _-> },
) {
    val esMama = rol.lowercase() == "mamá" || rol.lowercase() == "mama"
    val titulo = if (esMama) "Mi embarazo" else "Seguimiento"

    val (dia, semanaReal) = calcularDiaYSemana(fechaUltimaRegla)

    val trimestreNumero = obtenerTrimestreNumero(semanaReal)
    val trimestreTexto = when (trimestreNumero) {
        1 -> "Primer trimestre"
        2 -> "Segundo trimestre"
        3 -> "Tercer trimestre"
        else -> "Trimestre desconocido"
    }

    val favoritoRepository = remember { FavoritoSeguimientoRepository() }

    var cargando by remember { mutableStateOf(true) }
    var errorCarga by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val anuncios = remember { mutableStateListOf<AnuncioSeguimiento>() }
    val favoritos = remember { mutableStateListOf<String>() }

    var busqueda by remember { mutableStateOf("") }
    var soloFavoritos by remember { mutableStateOf(false) }
    var categoriaSeleccionada by remember { mutableStateOf<String?>(null) }

    val categoriasFiltro = listOf(
        null to "Todas",
        CategoriaInformativa.HIDRATACION to "💧 Hidratación",
        CategoriaInformativa.MASCOTAS to "🐾 Mascotas",
        CategoriaInformativa.ALIMENTACION to "🥦 Alimentación",
        CategoriaInformativa.SALUD to "🩺 Salud",
        CategoriaInformativa.RECURSOS to "📚 Recursos",
        CategoriaInformativa.DESCANSO to "😴 Descanso",
        CategoriaInformativa.BIENESTAR_EMOCIONAL to "🧘 Bienestar emocional",
        CategoriaInformativa.DESARROLLO_BEBE to "👶 Desarrollo del bebé"
    )

    LaunchedEffect(Unit) {
        getAnunciosSeguimiento { lista, error ->
            cargando = false

            if (error != null) {
                errorCarga = error
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            } else {
                errorCarga = null
                anuncios.clear()
                anuncios.addAll(lista)
            }
        }
    }

    LaunchedEffect(Unit) {
        favoritoRepository.obtenerFavoritos(
            onResultado = { lista ->
                favoritos.clear()
                favoritos.addAll(lista)
            },
            onError = { error ->
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            }
        )
    }

    val anunciosFiltrados = anuncios
        .filter { anuncio ->
            anuncio.semanaGestacion <= semanaReal &&
                    (categoriaSeleccionada == null || anuncio.categoria.equals(categoriaSeleccionada, ignoreCase = true)) &&
                    (
                            anuncio.titulo.contains(busqueda, ignoreCase = true) ||
                                    anuncio.contenido.contains(busqueda, ignoreCase = true) ||
                                    anuncio.categoria.contains(busqueda, ignoreCase = true)
                            )
        }
        .sortedByDescending { it.semanaGestacion }
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
        Box(modifier = Modifier.fillMaxSize()) {
            AuthBackground {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "🌸 $titulo 🌸",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = PurpleBlueText
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    WeekHighlightCard(
                        title = "Tu momento actual",
                        weekText = "Semana $semanaReal · Día $dia",
                        subtitle = trimestreTexto
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = busqueda,
                        onValueChange = { busqueda = it },
                        label = { Text("Buscar...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //Todas
                        FilterChip(
                            selected = categoriaSeleccionada == null && !soloFavoritos,
                            onClick = {
                                categoriaSeleccionada = null
                                soloFavoritos = false
                            },
                            label = { Text(text = "Todas", style = MaterialTheme.typography.labelMedium) }
                        )

                        //Favoritos
                        FilterChip(
                            selected = soloFavoritos,
                            onClick = { soloFavoritos = !soloFavoritos },
                            label = { Text(text = "⭐ Favoritos", style = MaterialTheme.typography.labelMedium) }
                        )

                        //Categorías
                        categoriasFiltro
                            .filter { (valor, _) -> valor != null } // excluye "Todas" si la tenías en la lista
                            .forEach { (valor, etiqueta) ->
                                FilterChip(
                                    selected = categoriaSeleccionada == valor,
                                    onClick = {
                                        categoriaSeleccionada = if (categoriaSeleccionada == valor) null else valor
                                        soloFavoritos = false // deselecciona Fav al elegir categoría
                                    },
                                    label = { Text(text = etiqueta, style = MaterialTheme.typography.labelMedium) }
                                )
                            }

                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (rol.lowercase() == "admin") {
                        PrimaryAuthButton(
                            text = "Añadir anuncio",
                            onClick = onAddAnuncioClick
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

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


                    anunciosFiltrados.forEach { anuncio ->
                        val esFavorito = favoritos.contains(anuncio.id)

                        if (!soloFavoritos || esFavorito) {
                            SweetSectionCard(
                                title = anuncio.titulo,
                                emoji = obtenerEmoji(anuncio.categoria),
                                text = anuncio.contenido.take(128) + "...",
                                esFavorito = esFavorito,
                                backgroundColor = obtenerColorCategoria(anuncio.categoria),
                                onFavoritoClick = {
                                    if (esFavorito) {
                                        favoritoRepository.quitarFavorito(
                                            idPublicacion = anuncio.id,
                                            onOk = { favoritos.remove(anuncio.id) },
                                            onError = { error ->
                                                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                                            }
                                        )
                                    } else {
                                        favoritoRepository.agregarFavorito(
                                            idPublicacion = anuncio.id,
                                            onOk = { favoritos.add(anuncio.id) },
                                            onError = { error ->
                                                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                                            }
                                        )
                                    }
                                },
                                onClick = {
                                    onAnuncioClick(
                                        anuncio.titulo,
                                        anuncio.categoria,
                                        anuncio.contenido,
                                        anuncio.semanaGestacion,
                                        anuncio.fuente,
                                        anuncio.imagenUrl
                                    )
                                },
                            )

                            Spacer(modifier = Modifier.height(14.dp))
                        }
                    }

                    if (!cargando && anunciosFiltrados.isEmpty()) {
                        SweetSectionCard(
                            title = "Aún no hay contenido",
                            emoji = "✨",
                            text = "Todavía no hay publicaciones disponibles para esta semana."
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    SoftDisclaimer(
                        text = "La información mostrada es orientativa y no sustituye la valoración de profesionales de la salud."
                    )
                }
            }
        }
    }
}

fun obtenerTrimestreNumero(semana: Int): Int {
    return when (semana) {
        in 1..13 -> 1
        in 14..27 -> 2
        in 28..40 -> 3
        else -> 0
    }
}

fun obtenerColorCategoria(categoria: String): Color {
    return when (categoria.lowercase().trim()) {
        "hidratacion" -> Color(0xFFD6EFFF)
        "mascotas" -> Color(0xFFE1BEE7)
        "alimentacion" -> Color(0xFFFFE0B2)
        "salud" -> Color(0xFFF8BBD0)
        "recursos" -> Color(0xFFFFECB3)
        "descanso" -> Color(0xFFE3F2FD)
        "bienestar_emocional" -> Color(0xFFE8F5E9)
        "desarrollo_bebe" -> Color(0xFFFFF3E0)
        else -> Color(0xFFE0E0E0)
    }
}


fun obtenerEmoji(categoria: String): String {
    return when (categoria) {
        CategoriaInformativa.HIDRATACION -> "💧"
        CategoriaInformativa.MASCOTAS -> "🐾"
        CategoriaInformativa.ALIMENTACION -> "🥦"
        CategoriaInformativa.SALUD -> "🩺"
        CategoriaInformativa.RECURSOS -> "📚"
        CategoriaInformativa.DESCANSO -> "😴"
        CategoriaInformativa.BIENESTAR_EMOCIONAL -> "🧘"
        CategoriaInformativa.DESARROLLO_BEBE -> "👶"
        else -> "✨"
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SeguimientoScreenPreview() {
    MaterialTheme {
        SeguimientoScreem(
            rol = "mamá",
            fechaUltimaRegla = 1735689600000L,
            onAnuncioClick = { _, _, _, _, _, _ -> }
        )
    }
}