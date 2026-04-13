package com.example.damagotchi_26.ui.MiEmbarazo

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                onTituloChange = { titulo = it },
                semanaGestacion = semanaGestacion,
                onSemanaChange = { semanaGestacion = it },
                categoria = categoria,
                onCategoriaChange = { categoria = it },
                contenido = contenido,
                onContenidoChange = { contenido = it },
                fuente = fuente,
                onFuenteChange = { fuente = it },
                urlFuente = urlFuente,
                onUrlFuenteChange = { urlFuente = it },
                onPublishClick = {
                    onPublishClick(
                        titulo,
                        semanaGestacion,
                        categoria,
                        contenido,
                        fuente,
                        urlFuente
                    )
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
    semanaGestacion: String,
    onSemanaChange: (String) -> Unit,
    categoria: String,
    onCategoriaChange: (String) -> Unit,
    contenido: String,
    onContenidoChange: (String) -> Unit,
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
            label = "Título"
        )

        Spacer(modifier = Modifier.height(14.dp))

        CampoAdmin(
            value = semanaGestacion,
            onValueChange = onSemanaChange,
            label = "Semana de gestación"
        )

        Spacer(modifier = Modifier.height(14.dp))

        CampoAdmin(
            value = categoria,
            onValueChange = onCategoriaChange,
            label = "Categoría"
        )

        Spacer(modifier = Modifier.height(14.dp))

        CampoAdmin(
            value = contenido,
            onValueChange = onContenidoChange,
            label = "Contenido del anuncio",
            singleLine = false,
            minLines = 5
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
            onClick = onPublishClick,
            modifier = Modifier.fillMaxWidth()
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
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = label)
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = singleLine,
        minLines = minLines,
        colors = TextFieldDefaults.colors()
    )
}

@Preview(showBackground = true)
@Composable
fun CrearAnuncioAdminPreview() {
    CrearAnuncioAdmin(
        onBack = {},
        onPublishClick = { _, _, _, _, _, _ -> }
    )
}