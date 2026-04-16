package com.example.damagotchi_26.ui.MiEmbarazo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.damagotchi_26.data.CategoriaInformativa
import com.example.damagotchi_26.ui.Color.Color.PurpleBlueText
import com.example.damagotchi_26.ui.components.AuthBackground
import com.example.damagotchi_26.ui.components.BackTextButton
import com.example.damagotchi_26.ui.components.PrimaryAuthButton

@Composable
fun CrearAnuncioAdmin(
    onBack: () -> Unit,
    onPublishClick: (
        titulo: String,
        semanaGestacion: String,
        categoria: String,
        contenido: String,
        fuente: String,
        urlFuente: String
    ) -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var semanaGestacion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var contenido by remember { mutableStateOf("") }
    var fuente by remember { mutableStateOf("") }
    var urlFuente by remember { mutableStateOf("") }

    var expandedCategoria by remember { mutableStateOf(false) }

    var tituloError by remember { mutableStateOf(false) }
    var semanaError by remember { mutableStateOf(false) }
    val semanaInt = semanaGestacion.toIntOrNull()

    semanaError = semanaGestacion.isBlank() ||
            semanaInt == null ||
            semanaInt !in 1..40
    var categoriaError by remember { mutableStateOf(false) }
    var contenidoError by remember { mutableStateOf(false) }

    val categorias = listOf(
        CategoriaInformativa.HIDRATACION,
        CategoriaInformativa.MASCOTAS,
        CategoriaInformativa.ALIMENTACION,
        CategoriaInformativa.SALUD,
        CategoriaInformativa.RECURSOS
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
            CrearAnuncioAdminContent(
                paddingValues = paddingValues,
                titulo = titulo,
                onTituloChange = {
                    titulo = it
                    tituloError = false
                },
                tituloError = tituloError,
                semanaGestacion = semanaGestacion,
                onSemanaChange = {
                    semanaGestacion = it.filter { c -> c.isDigit() }
                    semanaError = false
                },
                semanaError = semanaError,
                categoria = categoria,
                onCategoriaSelected = {
                    categoria = it
                    categoriaError = false
                },
                categoriaError = categoriaError,
                expandedCategoria = expandedCategoria,
                onExpandedCategoriaChange = { expandedCategoria = it },
                categorias = categorias,
                contenido = contenido,
                onContenidoChange = {
                    contenido = it
                    contenidoError = false
                },
                contenidoError = contenidoError,
                fuente = fuente,
                onFuenteChange = { fuente = it },
                urlFuente = urlFuente,
                onUrlFuenteChange = { urlFuente = it },
                onPublishClick = {
                    tituloError = titulo.isBlank()
                    semanaError = semanaGestacion.isBlank()
                    categoriaError = categoria.isBlank()
                    contenidoError = contenido.isBlank()

                    if (!tituloError && !semanaError && !categoriaError && !contenidoError) {
                        onPublishClick(
                            titulo.trim(),
                            semanaGestacion.trim(),
                            categoria.trim(),
                            contenido.trim(),
                            fuente.trim(),
                            urlFuente.trim()
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun CrearAnuncioAdminContent(
    paddingValues: PaddingValues,
    titulo: String,
    onTituloChange: (String) -> Unit,
    tituloError: Boolean,
    semanaGestacion: String,
    onSemanaChange: (String) -> Unit,
    semanaError: Boolean,
    categoria: String,
    onCategoriaSelected: (String) -> Unit,
    categoriaError: Boolean,
    expandedCategoria: Boolean,
    onExpandedCategoriaChange: (Boolean) -> Unit,
    categorias: List<String>,
    contenido: String,
    onContenidoChange: (String) -> Unit,
    contenidoError: Boolean,
    fuente: String,
    onFuenteChange: (String) -> Unit,
    urlFuente: String,
    onUrlFuenteChange: (String) -> Unit,
    onPublishClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Nuevo anuncio",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = PurpleBlueText,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Añade contenido validado para informar a las familias.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        CampoAdmin(
            value = titulo,
            onValueChange = onTituloChange,
            label = "Título",
            isError = tituloError,
            errorMessage = "Introduce un título"
        )

        Spacer(modifier = Modifier.height(14.dp))

        CampoAdmin(
            value = semanaGestacion,
            onValueChange = onSemanaChange,
            label = "Semana de gestación",
            isError = semanaError,
            errorMessage = "Introduce una semana entre 1 y 40"        )

        Spacer(modifier = Modifier.height(14.dp))

        CampoCategoriaAdmin(
            value = categoria,
            label = "Categoría",
            isError = categoriaError,
            errorMessage = "Selecciona una categoría",
            expanded = expandedCategoria,
            onExpandedChange = onExpandedCategoriaChange,
            categorias = categorias,
            onCategoriaSelected = onCategoriaSelected
        )

        Spacer(modifier = Modifier.height(14.dp))

        CampoAdmin(
            value = contenido,
            onValueChange = onContenidoChange,
            label = "Contenido del anuncio",
            singleLine = false,
            minLines = 5,
            isError = contenidoError,
            errorMessage = "Introduce el contenido del anuncio"
        )

        Spacer(modifier = Modifier.height(14.dp))

        CampoAdmin(
            value = fuente,
            onValueChange = onFuenteChange,
            label = "Nombre de la fuente"
        )

        Spacer(modifier = Modifier.height(14.dp))

        CampoAdmin(
            value = urlFuente,
            onValueChange = onUrlFuenteChange,
            label = "URL de la fuente"
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryAuthButton(
            text = "Publicar anuncio",
            onClick = onPublishClick
        )

        Spacer(modifier = Modifier.height(90.dp))
    }
}

@Composable
private fun CampoAdmin(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    singleLine: Boolean = true,
    minLines: Int = 1,
    isError: Boolean = false,
    errorMessage: String = ""
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = label) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            minLines = minLines,
            isError = isError,
            colors = TextFieldDefaults.colors()
        )

        if (isError) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}

@Composable
private fun CampoCategoriaAdmin(
    value: String,
    label: String,
    isError: Boolean,
    errorMessage: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    categorias: List<String>,
    onCategoriaSelected: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                label = { Text(label) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExpandedChange(true) },
                readOnly = true,
                isError = isError,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Desplegar categorías",
                        tint = Color.DarkGray,
                        modifier = Modifier.clickable {
                            onExpandedChange(!expanded)
                        }
                    )
                },
                colors = TextFieldDefaults.colors()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                categorias.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(formatearCategoria(opcion)) },
                        onClick = {
                            onCategoriaSelected(opcion)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }

        if (isError) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}

private fun formatearCategoria(categoria: String): String {
    return when (categoria) {
        CategoriaInformativa.HIDRATACION -> "Hidratación"
        CategoriaInformativa.MASCOTAS -> "Mascotas"
        CategoriaInformativa.ALIMENTACION -> "Alimentación"
        CategoriaInformativa.SALUD -> "Salud"
        CategoriaInformativa.RECURSOS -> "Recursos"
        else -> categoria
    }
}

@Preview(showBackground = true)
@Composable
fun CrearAnuncioAdminPreview() {
    CrearAnuncioAdmin(
        onBack = {},
        onPublishClick = { _, _, _, _, _, _ -> }
    )
}