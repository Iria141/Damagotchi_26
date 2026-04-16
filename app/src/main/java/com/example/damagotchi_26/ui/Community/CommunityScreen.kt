import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.damagotchi_26.ui.Color.Color.PurpleBlueText
import com.example.damagotchi_26.data.Post
import com.example.damagotchi_26.ui.components.AuthBackground
import com.example.damagotchi_26.ui.components.BackTextButton
import com.example.damagotchi_26.ui.components.PostCard
import com.example.damagotchi_26.ui.components.PrimaryAuthButton

@Composable
fun CommunityScreen(
    onCreatePostClick: () -> Unit, onPostClick: (String) -> Unit, onBack: () -> Unit = {}
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
        ), Post(
            id = "2",
            authorName = "Lucía",
            authorRole = "jugador",
            title = "¿Es normal el cansancio en la semana 12?",
            content = "Llevo varios días con mucho sueño y quería saber si os ha pasado.",
            type = "pregunta",
            isPinned = false
        )
    )

    val sortedPosts = posts.sortedByDescending { it.isPinned }


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
        }) { paddingValues ->
        AuthBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(top = 20.dp)
            ) {
                Text(
                    text = "\uD83C\uDF38Mi Comunidad\uD83C\uDF38",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurpleBlueText,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                PrimaryAuthButton(
                    text = "Crear publicación",
                    onClick = onCreatePostClick
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(sortedPosts) { post ->
                        PostCard(
                            post = post, onClick = { onPostClick(post.id) })
                    }
                }
                BackTextButton(onClick = onBack)
            }

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CommunityScreenPreview() {
    MaterialTheme {
        CommunityScreen(onCreatePostClick = {}, onPostClick = {}, onBack = {})
    }
}