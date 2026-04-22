package com.example.damagotchi_26.ui.Community

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.damagotchi_26.data.Post
import com.example.damagotchi_26.repository.LikeRepository
import com.example.damagotchi_26.repository.LikesPublicacionRepository
import com.example.damagotchi_26.repository.Notificacion
import com.example.damagotchi_26.repository.NotificacionesRepository
import com.example.damagotchi_26.repository.PublicacionesRepository
import com.example.damagotchi_26.repository.VisitaComunidadRepository
import com.example.damagotchi_26.ui.Color.Color.PurpleBlueText
import com.example.damagotchi_26.ui.Color.Color.PurpleBtn
import com.example.damagotchi_26.ui.components.AuthBackground
import com.example.damagotchi_26.ui.components.BackTextButton
import com.example.damagotchi_26.ui.components.PostCardExpandible
import com.example.damagotchi_26.ui.components.PrimaryAuthButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(
    rol: String = "jugador",
    nombre: String = "",
    onCreatePostClick: () -> Unit,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val publicacionesRepository = remember { PublicacionesRepository() }
    val visitaRepository = remember { VisitaComunidadRepository() }
    val notificacionesRepository = remember { NotificacionesRepository() }
    val posts = remember { mutableStateListOf<Post>() }
    var cargando by remember { mutableStateOf(true) }
    var busqueda by remember { mutableStateOf("") }
    var soloMisLikes by remember { mutableStateOf(false) }
    val misLikes = remember { mutableStateListOf<String>() }
    val likeRepository = remember { LikeRepository() }
    val likesPublicacionRepository = remember { LikesPublicacionRepository() }
    var ultimaVisita by remember { mutableStateOf(0L) }
    var publicacionesNuevas by remember { mutableStateOf(0) }
    val notificaciones = remember { mutableStateListOf<Notificacion>() }
    var mostrarNotificaciones by remember { mutableStateOf(false) }
    val noLeidas = notificaciones.count { !it.leida }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var postExpandidoId by remember { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        notificacionesRepository.obtenerNotificaciones(
            onResultado = { lista ->
                notificaciones.clear()
                notificaciones.addAll(lista)
            },
            onError = {}
        )
    }

    LaunchedEffect(Unit) {
        likesPublicacionRepository.obtenerPostsConLike(
            onResultado = { ids ->
                misLikes.clear()
                misLikes.addAll(ids)
            },
            onError = {}
        )
    }

    LaunchedEffect(Unit) {
        visitaRepository.obtenerUltimaVisita(
            onResultado = { timestamp ->
                ultimaVisita = timestamp
            },
            onError = {}
        )
    }

    LaunchedEffect(Unit) {
        publicacionesRepository.obtenerPosts(
            onResultado = { lista ->
                posts.clear()
                posts.addAll(lista)
                publicacionesNuevas = lista.count { it.createdAt > ultimaVisita && ultimaVisita > 0L }
                cargando = false
                visitaRepository.actualizarUltimaVisita()
            },
            onError = { error ->
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                cargando = false
            }
        )
    }

    val postsFiltrados = posts.filter { post ->
        (!soloMisLikes || misLikes.contains(post.id)) &&
                (busqueda.isBlank() ||
                        post.title.contains(busqueda, ignoreCase = true) ||
                        post.content.contains(busqueda, ignoreCase = true) ||
                        post.authorName.contains(busqueda, ignoreCase = true))
    }

    // Scroll automático cuando se selecciona un post desde notificaciones
    LaunchedEffect(postExpandidoId, postsFiltrados.size) {
        val id = postExpandidoId ?: return@LaunchedEffect
        val index = postsFiltrados.indexOfFirst { it.id == id }
        if (index >= 0) {
            coroutineScope.launch {
                listState.animateScrollToItem(index)
            }
        }
    }

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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Título con campanita
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.size(48.dp))

                    Text(
                        text = "🌸 Mi Comunidad 🌸",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = PurpleBlueText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )

                    Box {
                        IconButton(onClick = {
                            mostrarNotificaciones = true
                            notificacionesRepository.marcarTodasComoLeidas(
                                onOk = {
                                    notificaciones.replaceAll { it.copy(leida = true) }
                                }
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Notifications,
                                contentDescription = "Notificaciones",
                                tint = PurpleBtn
                            )
                        }
                        if (noLeidas > 0) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .background(Color.Red, CircleShape)
                                    .align(Alignment.TopEnd),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (noLeidas > 9) "9+" else "$noLeidas",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                PrimaryAuthButton(
                    text = "✏️ Crear publicación",
                    onClick = onCreatePostClick
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Buscador con ❤️ integrado a la derecha
                OutlinedTextField(
                    value = busqueda,
                    onValueChange = { busqueda = it },
                    label = { Text("Buscar...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    trailingIcon = {
                        IconButton(onClick = { soloMisLikes = !soloMisLikes }) {
                            Icon(
                                imageVector = if (soloMisLikes) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Mis likes",
                                tint = if (soloMisLikes) Color(0xFFE91E63) else Color.Gray
                            )
                        }
                    }
                )

                if (cargando) {
                    Text(
                        text = "Cargando publicaciones...",
                        color = PurpleBlueText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (!cargando) {
                    Text(
                        text = if (publicacionesNuevas > 0)
                            "✨ ${posts.size} publicaciones · $publicacionesNuevas nuevas desde tu última visita"
                        else
                            "✨ ${posts.size} ${if (posts.size == 1) "publicación" else "publicaciones"}",
                        color = PurpleBtn,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (!cargando) {
                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp)
                    ) {
                        if (postsFiltrados.isEmpty()) {
                            item {
                                Text(
                                    text = if (soloMisLikes)
                                        "Aún no has dado like a ninguna publicación"
                                    else if (busqueda.isNotBlank())
                                        "No hay publicaciones con esa búsqueda"
                                    else
                                        "Aún no hay publicaciones. ¡Sé el primero!",
                                    color = PurpleBlueText,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 32.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            items(postsFiltrados.size) { index ->
                                val post = postsFiltrados[index]
                                PostCardExpandible(
                                    post = post,
                                    rol = rol,
                                    nombre = nombre,
                                    index = index,
                                    expandidoExterno = postExpandidoId == post.id,
                                    onLikeChanged = { postId, diLike ->
                                        if (diLike) misLikes.add(postId)
                                        else misLikes.remove(postId)
                                    }
                                )
                            }
                        }

                        item { Spacer(modifier = Modifier.height(16.dp)) }
                    }
                }
            }

            // Panel de notificaciones
            if (mostrarNotificaciones) {
                ModalBottomSheet(
                    onDismissRequest = { mostrarNotificaciones = false },
                    sheetState = sheetState
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 32.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🔔 Notificaciones",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = PurpleBlueText
                            )
                            TextButton(onClick = { mostrarNotificaciones = false }) {
                                Text(text = "Cerrar", color = PurpleBtn)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (notificaciones.isEmpty()) {
                            Text(
                                text = "No tienes notificaciones aún",
                                color = Color.Gray,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                        } else {
                            notificaciones.forEach { notificacion ->
                                NotificacionItem(
                                    notificacion = notificacion,
                                    onClick = {
                                        postExpandidoId = notificacion.postId
                                        mostrarNotificaciones = false
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificacionItem(
    notificacion: Notificacion,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notificacion.leida)
                Color(0xFFF0F0F0)
            else
                Color(0xFFEDE7F6)
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = if (notificacion.tipo == "nueva_publicacion") "🌸" else "💬",
                fontSize = 20.sp
            )
            Column {
                Text(
                    text = notificacion.titulo,
                    fontWeight = if (notificacion.leida) FontWeight.Normal else FontWeight.Bold,
                    fontSize = 14.sp,
                    color = PurpleBlueText
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = notificacion.cuerpo,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }
    }
}