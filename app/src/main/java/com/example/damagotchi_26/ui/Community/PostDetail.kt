package com.example.damagotchi_26.ui.Community

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.damagotchi_26.data.Post
import com.example.damagotchi_26.ui.Color.Color.PurpleBlueText
import com.example.damagotchi_26.ui.components.AuthBackground
import com.example.damagotchi_26.ui.components.BackTextButton

@Composable
fun PostDetailScreen(
    postId: String,
    onBack: () -> Unit
) {
    val posts = listOf(
        Post(
            id = "1",
            authorName = "Admin",
            authorRole = "admin",
            title = "Bienvenidos al tablón",
            content = "Aquí podéis compartir dudas, consultas y opiniones.",
            type = "anuncio",
            isPinned = true
        ),
        Post(
            id = "2",
            authorName = "Lucía",
            authorRole = "jugador",
            title = "¿Es normal estar cansada?",
            content = "Estoy en la semana 12 y me siento muy cansada.",
            type = "pregunta",
            isPinned = false
        )
    )

    val post = posts.find { it.id == postId }

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                BackTextButton(onClick = onBack)
            }
        }
    ) { paddingValues ->
        AuthBackground {
            if (post == null) {
                EmptyPostDetailContent(
                    paddingValues = paddingValues
                )
            } else {
                PostDetailContent(
                    post = post,
                    paddingValues = paddingValues
                )
            }
        }
    }
}

@Composable
private fun EmptyPostDetailContent(
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Publicación no encontrada",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PurpleBlueText
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "No se ha podido cargar el detalle de esta publicación.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun PostDetailContent(
    post: Post,
    paddingValues: PaddingValues
) {
    val isAdmin = post.authorRole.lowercase() == "admin"

    val icon = when (post.type.lowercase()) {
        "anuncio" -> "📢"
        "pregunta" -> "❓"
        "opinion" -> "💬"
        else -> "📝"
    }

    val tipoTexto = when (post.type.lowercase()) {
        "anuncio" -> "ANUNCIO"
        "pregunta" -> "PREGUNTA"
        "opinion" -> "OPINIÓN"
        else -> post.type.uppercase()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Detalle de publicación",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = PurpleBlueText,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isAdmin) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
                } else {
                    MaterialTheme.colorScheme.surface
                }
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                if (post.isPinned) {
                    Text(
                        text = "📌 Publicación destacada",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                Text(
                    text = "$icon ${post.title}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = PurpleBlueText
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Por: ${post.authorName}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = tipoTexto,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = post.content,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostDetailScreenPreview() {
    PostDetailScreen(
        postId = "1",
        onBack = {}
    )
}