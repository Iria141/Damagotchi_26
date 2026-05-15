package com.example.damagotchi_26.ui.Configuracion

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.damagotchi_26.ui.Color.Color.PurpleBlueText
import com.example.damagotchi_26.ui.Color.Color.PurpleBtn
import com.example.damagotchi_26.ui.components.AuthBackground
import com.example.damagotchi_26.ui.components.BackTextButton
import com.example.damagotchi_26.ui.components.ConfigCard
import com.example.damagotchi_26.ui.components.FilaClickable
import com.example.damagotchi_26.ui.components.FilaSwitch
import com.example.damagotchi_26.ui.components.PrimaryAuthButton
import com.example.damagotchi_26.ui.components.SeccionHeader
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun ConfiguracionScreen(
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val uid = auth.currentUser?.uid

    // Estado perfil
    var fechaNacimiento by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var fechaUltimaRegla by remember { mutableStateOf("") }
    var fechaUltimaReglaTimestamp by remember { mutableStateOf(0L) }
    var sexoBebe by remember { mutableStateOf("") }
    var expandedSexo by remember { mutableStateOf(false) }

    // Estado notificaciones
    var notifNuevaPublicacion by remember { mutableStateOf(true) }
    var notifNuevoComentario by remember { mutableStateOf(true) }

    // Estado audio
    var musicaActivada by remember { mutableStateOf(true) }
    var sonidosActivados by remember { mutableStateOf(true) }

    // Estado general
    var idioma by remember { mutableStateOf("es") }
    var expandedIdioma by remember { mutableStateOf(false) }
    var usarAlias by remember { mutableStateOf(false) }
    var alias by remember { mutableStateOf("") }
    var mostrarConfirmarEliminar by remember { mutableStateOf(false) }

    val idiomas = listOf(
        "es" to "🇪🇸 Español",
        "ca" to "🏴 Català",
        "en" to "🇬🇧 English"
    )

    var guardando by remember { mutableStateOf(false) }
    var tabSeleccionada by remember { mutableIntStateOf(0) }

    val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    LaunchedEffect(uid) {
        if (uid == null) return@LaunchedEffect
        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                fechaNacimiento = doc.getString("fechaNacimiento") ?: ""
                nombre = doc.getString("nombre") ?: ""
                sexoBebe = doc.getString("sexoBebe") ?: ""
                val timestamp = doc.getLong("fechaUltimaRegla") ?: 0L
                if (timestamp > 0L) {
                    fechaUltimaReglaTimestamp = timestamp
                    fechaUltimaRegla = formatoFecha.format(Date(timestamp))
                }
                notifNuevaPublicacion = doc.getBoolean("notificaciones_nueva_publicacion") ?: true
                notifNuevoComentario = doc.getBoolean("notificaciones_nuevo_comentario") ?: true
                idioma = doc.getString("idioma") ?: "es"
                usarAlias = doc.getBoolean("usar_alias") ?: false
                alias = doc.getString("alias") ?: ""
                musicaActivada = doc.getBoolean("musica_activada") ?: true
                sonidosActivados = doc.getBoolean("sonidos_activados") ?: true
            }
    }

    val calNac = Calendar.getInstance()
    val datePickerNacimiento = DatePickerDialog(
        context,
        { _, year, month, day ->
            fechaNacimiento = "%02d/%02d/%04d".format(day, month + 1, year)
        },
        calNac.get(Calendar.YEAR),
        calNac.get(Calendar.MONTH),
        calNac.get(Calendar.DAY_OF_MONTH)
    )

    val calRegla = Calendar.getInstance()
    val datePickerRegla = DatePickerDialog(
        context,
        { _, year, month, day ->
            val cal = Calendar.getInstance().apply { set(year, month, day) }
            fechaUltimaReglaTimestamp = cal.timeInMillis
            fechaUltimaRegla = "%02d/%02d/%04d".format(day, month + 1, year)
        },
        calRegla.get(Calendar.YEAR),
        calRegla.get(Calendar.MONTH),
        calRegla.get(Calendar.DAY_OF_MONTH)
    )

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
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "⚙️ Configuración",
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = PurpleBlueText,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                TabRow(
                    selectedTabIndex = tabSeleccionada,
                    containerColor = Color.Transparent,
                    contentColor = PurpleBtn,
                    indicator = { tabPositions ->
                        SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[tabSeleccionada]),
                            color = PurpleBtn
                        )
                    }
                ) {
                    Tab(
                        selected = tabSeleccionada == 0,
                        onClick = { tabSeleccionada = 0 },
                        text = {
                            Text(
                                text = "General",
                                fontWeight = if (tabSeleccionada == 0) FontWeight.Bold else FontWeight.Normal,
                                color = if (tabSeleccionada == 0) PurpleBtn else PurpleBlueText
                            )
                        }
                    )
                    Tab(
                        selected = tabSeleccionada == 1,
                        onClick = { tabSeleccionada = 1 },
                        text = {
                            Text(
                                text = "Mi perfil",
                                fontWeight = if (tabSeleccionada == 1) FontWeight.Bold else FontWeight.Normal,
                                color = if (tabSeleccionada == 1) PurpleBtn else PurpleBlueText
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    when (tabSeleccionada) {

                        0 -> {
                            // Notificaciones
                            SeccionHeader(titulo = "🔔 Notificaciones")
                            Spacer(modifier = Modifier.height(12.dp))
                            ConfigCard {
                                FilaSwitch(
                                    titulo = "Nueva publicación",
                                    descripcion = "Aviso cuando alguien publique en la comunidad",
                                    checked = notifNuevaPublicacion,
                                    onCheckedChange = { notifNuevaPublicacion = it }
                                )
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    color = Color.LightGray.copy(alpha = 0.5f)
                                )
                                FilaSwitch(
                                    titulo = "Nueva respuesta",
                                    descripcion = "Aviso cuando alguien responda en la comunidad",
                                    checked = notifNuevoComentario,
                                    onCheckedChange = { notifNuevoComentario = it }
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Audio
                            SeccionHeader(titulo = "🎵 Audio")
                            Spacer(modifier = Modifier.height(12.dp))
                            ConfigCard {
                                FilaSwitch(
                                    titulo = "Música de fondo",
                                    descripcion = "Música ambiental durante el modo juego",
                                    checked = musicaActivada,
                                    onCheckedChange = { musicaActivada = it }
                                )
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    color = Color.LightGray.copy(alpha = 0.5f)
                                )
                                FilaSwitch(
                                    titulo = "Sonidos",
                                    descripcion = "Efectos de sonido en las acciones y minijuegos",
                                    checked = sonidosActivados,
                                    onCheckedChange = { sonidosActivados = it }
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Idioma
                            SeccionHeader(titulo = "🌍 Idioma")
                            Spacer(modifier = Modifier.height(12.dp))
                            ConfigCard {
                                Text(
                                    text = "La traducción completa estará disponible próximamente",
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    OutlinedTextField(
                                        value = idiomas.find { it.first == idioma }?.second ?: "🇪🇸 Español",
                                        onValueChange = {},
                                        label = { Text("Idioma") },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { expandedIdioma = true },
                                        readOnly = true,
                                        trailingIcon = {
                                            Icon(
                                                imageVector = Icons.Filled.ArrowDropDown,
                                                contentDescription = "Seleccionar idioma",
                                                modifier = Modifier.clickable { expandedIdioma = true }
                                            )
                                        }
                                    )
                                    DropdownMenu(
                                        expanded = expandedIdioma,
                                        onDismissRequest = { expandedIdioma = false }
                                    ) {
                                        idiomas.forEach { (valor, etiqueta) ->
                                            DropdownMenuItem(
                                                text = { Text(etiqueta) },
                                                onClick = {
                                                    idioma = valor
                                                    expandedIdioma = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Privacidad
                            SeccionHeader(titulo = "👁️ Privacidad")
                            Spacer(modifier = Modifier.height(12.dp))
                            ConfigCard {
                                FilaSwitch(
                                    titulo = "Usar alias en la comunidad",
                                    descripcion = "Tu nombre real no será visible en publicaciones",
                                    checked = usarAlias,
                                    onCheckedChange = { usarAlias = it }
                                )
                                if (usarAlias) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = alias,
                                        onValueChange = { if (it.length <= 20) alias = it },
                                        label = { Text("Tu alias (${alias.length}/20)") },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Cuenta
                            SeccionHeader(titulo = "🗑️ Cuenta")
                            Spacer(modifier = Modifier.height(12.dp))
                            ConfigCard {
                                if (!mostrarConfirmarEliminar) {
                                    FilaClickable(
                                        titulo = "Eliminar cuenta",
                                        descripcion = "Esta acción no se puede deshacer",
                                        tituloColor = Color(0xFFB00020),
                                        onClick = { mostrarConfirmarEliminar = true }
                                    )
                                } else {
                                    Text(
                                        text = "¿Estás segura? Se eliminarán todos tus datos.",
                                        fontSize = 13.sp,
                                        color = Color(0xFFB00020),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                        androidx.compose.material3.OutlinedButton(
                                            onClick = { mostrarConfirmarEliminar = false },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Cancelar", color = PurpleBlueText)
                                        }
                                        androidx.compose.material3.Button(
                                            onClick = {
                                                auth.currentUser?.delete()
                                                    ?.addOnSuccessListener {
                                                        if (uid != null) {
                                                            db.collection("users").document(uid).delete()
                                                        }
                                                        Toast.makeText(context, "Cuenta eliminada", Toast.LENGTH_LONG).show()
                                                    }
                                                    ?.addOnFailureListener { e ->
                                                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                                                    }
                                            },
                                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFFB00020)
                                            ),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Eliminar", color = Color.White)
                                        }
                                    }
                                }
                            }
                        }

                        1 -> {
                            SeccionHeader(titulo = "👤 Datos personales")
                            Spacer(modifier = Modifier.height(12.dp))
                            ConfigCard {
                                OutlinedTextField(
                                    value = nombre,
                                    onValueChange = { nombre = it },
                                    label = { Text("Nombre") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                OutlinedTextField(
                                    value = fechaNacimiento,
                                    onValueChange = {},
                                    label = { Text("Fecha de nacimiento") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { datePickerNacimiento.show() },
                                    readOnly = true,
                                    trailingIcon = {
                                        Icon(
                                            imageVector = Icons.Filled.CalendarMonth,
                                            contentDescription = "Seleccionar fecha",
                                            tint = PurpleBtn,
                                            modifier = Modifier.clickable { datePickerNacimiento.show() }
                                        )
                                    }
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                OutlinedTextField(
                                    value = fechaUltimaRegla,
                                    onValueChange = {},
                                    label = { Text("Fecha última regla") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { datePickerRegla.show() },
                                    readOnly = true,
                                    trailingIcon = {
                                        Icon(
                                            imageVector = Icons.Filled.CalendarMonth,
                                            contentDescription = "Seleccionar fecha",
                                            tint = PurpleBtn,
                                            modifier = Modifier.clickable { datePickerRegla.show() }
                                        )
                                    }
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    OutlinedTextField(
                                        value = when (sexoBebe) {
                                            "niño" -> "👦 Niño"
                                            "niña" -> "👧 Niña"
                                            "sorpresa" -> "🎁 Sorpresa"
                                            else -> ""
                                        },
                                        onValueChange = {},
                                        label = { Text("Sexo del bebé") },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { expandedSexo = true },
                                        readOnly = true,
                                        trailingIcon = {
                                            Icon(
                                                imageVector = Icons.Filled.ArrowDropDown,
                                                contentDescription = "Seleccionar",
                                                modifier = Modifier.clickable { expandedSexo = true }
                                            )
                                        }
                                    )
                                    DropdownMenu(
                                        expanded = expandedSexo,
                                        onDismissRequest = { expandedSexo = false }
                                    ) {
                                        listOf(
                                            "niño" to "👦 Niño",
                                            "niña" to "👧 Niña",
                                            "sorpresa" to "🎁 Sorpresa"
                                        ).forEach { (valor, etiqueta) ->
                                            DropdownMenuItem(
                                                text = { Text(etiqueta) },
                                                onClick = {
                                                    sexoBebe = valor
                                                    expandedSexo = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    PrimaryAuthButton(
                        text = if (guardando) "Guardando..." else "Guardar cambios",
                        onClick = {
                            if (uid == null) return@PrimaryAuthButton
                            guardando = true
                            val datos = mapOf(
                                "nombre" to nombre,
                                "fechaNacimiento" to fechaNacimiento,
                                "fechaUltimaRegla" to fechaUltimaReglaTimestamp,
                                "sexoBebe" to sexoBebe,
                                "notificaciones_nueva_publicacion" to notifNuevaPublicacion,
                                "notificaciones_nuevo_comentario" to notifNuevoComentario,
                                "idioma" to idioma,
                                "usar_alias" to usarAlias,
                                "alias" to alias,
                                "musica_activada" to musicaActivada,
                                "sonidos_activados" to sonidosActivados
                            )
                            db.collection("users").document(uid)
                                .update(datos)
                                .addOnSuccessListener {
                                    guardando = false
                                    Toast.makeText(context, "✅ Cambios guardados", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    guardando = false
                                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}