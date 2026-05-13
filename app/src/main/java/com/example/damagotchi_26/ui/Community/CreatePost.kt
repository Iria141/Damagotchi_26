package com.example.damagotchi_26.ui.Community

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.FilterChip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.damagotchi_26.repository.PublicacionesRepository
import com.example.damagotchi_26.ui.Color.Color.CardGray
import com.example.damagotchi_26.ui.Color.Color.PurpleBlueText
import com.example.damagotchi_26.ui.Color.Color.PurpleBtn
import com.example.damagotchi_26.ui.components.AuthBackground
import com.example.damagotchi_26.ui.components.AuthCard
import com.example.damagotchi_26.ui.components.AuthTextField
import com.example.damagotchi_26.ui.components.BackTextButton
import com.example.damagotchi_26.ui.components.PrimaryAuthButton

@Composable
fun CreatePostScreen(
    rol: String = "jugador",
    nombre: String = "",
    onPublishOk: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val postRepository = remember { PublicacionesRepository() }

    var titulo by remember { mutableStateOf("") }
    var contenido by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("pregunta") }
    var tituloError by remember { mutableStateOf(false) }
    var contenidoError by remember { mutableStateOf(false) }
    var publicando by remember { mutableStateOf(false) }

    val isAdmin = rol.lowercase() == "admin"
    val availableTypes = if (isAdmin) {
        listOf("pregunta", "opinion", "anuncio")
    } else {
        listOf("pregunta", "opinion")
    }

    val maxTitulo = 50
    val maxContenido = 250

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
                    .padding(top = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Nueva publicación",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurpleBlueText
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Comparte una duda, una opinión o una experiancia  con la comunidad",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                AuthCard(modifier = Modifier.fillMaxWidth()) {

                    AuthTextField(
                        value = titulo,
                        onValueChange = {
                            if (it.length <= maxTitulo) {
                                titulo = it
                                tituloError = false
                            }
                        },
                        label = "Título (${titulo.length}/$maxTitulo)"
                    )
                    if (tituloError) {
                        Text(
                            text = "El título no puede estar vacío",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
                        )
                    }

                    AuthTextField(
                        value = contenido,
                        onValueChange = {
                            if (it.length <= maxContenido) {
                                contenido = it
                                contenidoError = false
                            }
                        },
                        label = "Contenido (${contenido.length}/$maxContenido)",
                        singleLine = false,
                        minLines = 2
                    )
                    if (contenidoError) {
                        Text(
                            text = "El contenido no puede estar vacío",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Tipo de publicación",
                        modifier = Modifier.padding(horizontal = 15.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PurpleBlueText
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        availableTypes.forEach { type ->
                            FilterChip(
                                selected = selectedType == type,
                                onClick = { selectedType = type },

                                label = {
                                    Text(
                                        text = type.replaceFirstChar { it.uppercase() },
                                        fontWeight = if (selectedType == type)
                                            FontWeight.Bold
                                        else
                                            FontWeight.Normal,

                                        fontSize = 14.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                },

                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp) // ← más altura
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    PrimaryAuthButton(
                        text = if (publicando) "Publicando..." else "Publicar",
                        onClick = {
                            tituloError = titulo.isBlank()
                            contenidoError = contenido.isBlank()

                            if (!tituloError && !contenidoError && !publicando) {
                                publicando = true
                                postRepository.crearPost(
                                    titulo = titulo.trim(),
                                    contenido = contenido.trim(),
                                    tipo = selectedType,
                                    authorName = nombre,
                                    authorRole = rol,
                                    onOk = {
                                        publicando = false
                                        onPublishOk()
                                    },
                                    onError = { error ->
                                        publicando = false
                                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                                    }
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreatePostScreenPreview() {
    MaterialTheme {
        CreatePostScreen(rol = "admin", nombre = "Admin")
    }
}