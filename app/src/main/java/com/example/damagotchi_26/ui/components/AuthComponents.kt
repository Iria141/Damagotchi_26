package com.example.damagotchi_26.ui.components

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import com.example.damagotchi_26.data.Comment
import com.example.damagotchi_26.repository.ComentarioRepository
import com.example.damagotchi_26.repository.LikeRepository
import com.example.damagotchi_26.ui.Color.Color.BorderGray
import com.example.damagotchi_26.ui.Color.Color.CardGray
import com.example.damagotchi_26.ui.Color.Color.PurpleBlueText
import com.example.damagotchi_26.ui.Color.Color.PurpleBtn
import com.example.damagotchi_26.data.Post
import com.example.damagotchi_26.ui.Color.Color.CardWhiteRose
import com.example.damagotchi_26.ui.Color.Color.YellowStar


@Composable
fun AuthBackground(
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(com.example.damagotchi_26.ui.Color.Color.PinkBg),
        content = content
    )
}

val textStyle = androidx.compose.ui.text.TextStyle(
    fontSize = 14.sp,
    color = PurpleBlueText,
    fontWeight = FontWeight.Normal
)

@Composable
fun AuthCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(com.example.damagotchi_26.ui.Color.Color.PinkBg, RoundedCornerShape(30.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        content = content
    )
}

@Composable
fun PrimaryAuthButton(
    text: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = com.example.damagotchi_26.ui.Color.Color.PurpleBtn),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .padding(horizontal = 50.dp)
    ) {
        Text(text, color = Color.White)
    }
}


@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    isError: Boolean = false,
    errorMessage: String = "",
    trailingIcon: (@Composable (() -> Unit))? = null, //añadir icono al calendario
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    modifier: Modifier = Modifier

) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,

            label = {
                Text(
                    text = label,
                    fontSize = 18.sp
                )
            },
            singleLine = singleLine, //solo una linea, no permite salto de linea

            visualTransformation =
                if (isPassword) PasswordVisualTransformation()
                else VisualTransformation.None,

            isError = isError,
            trailingIcon = trailingIcon,
            readOnly = readOnly,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),


            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),

            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CardWhiteRose,
                unfocusedContainerColor = CardWhiteRose,
                disabledContainerColor = CardWhiteRose,

                focusedBorderColor = CardWhiteRose,
                unfocusedBorderColor = CardWhiteRose,
                disabledBorderColor = CardWhiteRose,

                focusedLabelColor = Color.Gray,
                unfocusedLabelColor = Color.Gray,

                cursorColor = Color.DarkGray
            )
        )

        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 20.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun AuthInfoBox(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(CardGray, RoundedCornerShape(4.dp))
            .border(1.dp, BorderGray, RoundedCornerShape(4.dp))
            .padding(18.dp)
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
    }
}

@Composable
private fun PostTypeChip(
    text: String,
    isAdmin: Boolean
) {
    Surface(
        color = if (isAdmin) PurpleBtn.copy(alpha = 0.14f) else CardGray,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text.uppercase(),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            color = if (isAdmin) PurpleBtn else PurpleBlueText,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun PostCard(
    post: Post,
    onClick: () -> Unit
) {
    val isAdmin = post.authorRole == "admin"

    val icon = when (post.type) {
        "anuncio" -> "📢"
        "pregunta" -> "❓"
        "opinion" -> "💬"
        else -> "📝"
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isAdmin) Color.White else CardGray
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "$icon ${post.title}",
                    style = MaterialTheme.typography.titleMedium,
                    color = PurpleBlueText,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )

                if (post.isPinned) {
                    Text(
                        text = "📌",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Text(
                text = "Por: ${post.authorName}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PostTypeChip(
                    text = post.type,
                    isAdmin = isAdmin
                )

                if (isAdmin) {
                    PostTypeChip(
                        text = "admin",
                        isAdmin = true
                    )
                }
            }

            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                color = PurpleBlueText,
                maxLines = 3
            )
        }
    }
}

@Composable
fun MenuOptionCard(
    title: String,
    subtitle: String,
    emoji: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = CardWhiteRose),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp)
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = emoji,
                fontSize = 26.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PurpleBlueText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

@Composable
fun WeekHighlightCard(
    title: String,
    weekText: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = PurpleBtn.copy(alpha = 0.16f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                fontSize = 15.sp,
                color = PurpleBlueText
            )

            Text(
                text = weekText,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = PurpleBlueText
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = PurpleBlueText
            )
        }
    }
}
@Composable
fun SweetSectionCard(
    title: String,
    emoji: String,
    text: String,
    modifier: Modifier = Modifier,
    esFavorito: Boolean = false,
    onClick: () -> Unit = {},
    backgroundColor: Color = Color(0xFFF5F5F5),
    onFavoritoClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "$emoji  $title",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PurpleBlueText,
                    modifier = Modifier.weight(1f)
                )

                if (onFavoritoClick != null) {
                    IconButton(onClick = onFavoritoClick) {
                        Icon(
                            imageVector = if (esFavorito) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = "Favorito",
                            tint = if (esFavorito) YellowStar else PurpleBlueText
                        )
                    }
                }
            }

            Text(
                text = text,
                color = Color(0xFF5F5A66),
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun SoftDisclaimer(
    text: String,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(
                color = Color.White.copy(alpha = 0.65f),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = PurpleBlueText,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )
    }
}

@Composable
fun BackTextButton(
    onClick: () -> Unit
) {
    val textStyle = TextStyle(
        fontSize = 16.sp,
        color = PurpleBlueText,
        fontWeight = FontWeight.Normal
    )

    TextButton(onClick = onClick) {
        Text(
            text = "← Volver",
            style = textStyle
        )
    }
}

fun formatearFecha(timestamp: Long): String {
    if (timestamp == 0L) return ""
    val ahora = System.currentTimeMillis()
    val diferencia = ahora - timestamp
    val minutos = diferencia / 60_000
    val horas = diferencia / 3_600_000
    val dias = diferencia / 86_400_000

    val calAhora = Calendar.getInstance()
    val calFecha = Calendar.getInstance().apply { timeInMillis = timestamp }
    val esAyer = calAhora.get(Calendar.DAY_OF_YEAR) - calFecha.get(Calendar.DAY_OF_YEAR) == 1
            && calAhora.get(Calendar.YEAR) == calFecha.get(Calendar.YEAR)
    val mismoAnio = calAhora.get(Calendar.YEAR) == calFecha.get(Calendar.YEAR)

    val hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp))
    val fechaCorta = SimpleDateFormat("d MMM", Locale("es")).format(Date(timestamp))
    val fechaLarga = SimpleDateFormat("d MMM yyyy", Locale("es")).format(Date(timestamp))

    return when {
        minutos < 1 -> "ahora"
        minutos < 60 -> "hace $minutos min"
        horas < 24 -> "hace $horas ${if (horas == 1L) "hora" else "horas"}"
        esAyer -> "ayer · $hora"
        mismoAnio -> "$fechaCorta · $hora"
        else -> "$fechaLarga · $hora"
    }
}


@Composable
fun PostCardExpandible(
    post: Post,
    rol: String = "jugador",
    nombre: String = "",
    index: Int = 0,
    expandidoExterno: Boolean = false,
    onLikeChanged: (postId: String, diLike: Boolean) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    val comentarioRepository = remember { ComentarioRepository() }
    val likeRepository = remember { LikeRepository() }
    val comentarios = remember { mutableStateListOf<Comment>() }

    var expandido by remember(expandidoExterno) { mutableStateOf(expandidoExterno) }
    var comentariosCargados by remember { mutableStateOf(false) }
    var nuevoComentario by remember { mutableStateOf("") }
    var enviando by remember { mutableStateOf(false) }

    var likesPost by remember { mutableStateOf(0) }
    var yoDiLikePost by remember { mutableStateOf(false) }

    val isAdmin = post.authorRole.lowercase() == "admin"

    val icon = when (post.type.lowercase()) {
        "anuncio" -> "📢"
        "pregunta" -> "❓"
        "opinion" -> "💬"
        else -> "📝"
    }

    // Carga likes del post
    LaunchedEffect(post.id) {
        likeRepository.obtenerLikesPost(
            postId = post.id,
            onResultado = { total, yoDiLike ->
                likesPost = total
                yoDiLikePost = yoDiLike
            },
            onError = {}
        )
    }

    // Carga comentarios la primera vez que se expande
    LaunchedEffect(expandido) {
        if (expandido && !comentariosCargados) {
            comentarioRepository.obtenerComentarios(
                postId = post.id,
                onResultado = { lista ->
                    comentarios.clear()
                    comentarios.addAll(lista)
                    comentariosCargados = true
                },
                onError = { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    // Colores alternos atractivos, admin siempre blanco
    val colorCard = when {
        isAdmin -> Color.White
        index % 2 == 0 -> Color(0xFFF5EFE6) // beige cálido
        else -> Color(0xFFEEF0F5)            // gris azulado suave
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = colorCard),
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Cabecera
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "$icon ${post.title}",
                    style = MaterialTheme.typography.titleMedium,
                    color = PurpleBlueText,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                if (post.isPinned) {
                    Text(
                        text = "📌",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Por: ${post.authorName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = formatearFecha(post.createdAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PostTypeChip(text = post.type, isAdmin = isAdmin)
                if (isAdmin) PostTypeChip(text = "admin", isAdmin = true)
            }

            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                color = PurpleBlueText
            )

            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

            // Fila de acciones: like + respuestas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón like
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = {
                            likeRepository.toggleLikePost(
                                postId = post.id,
                                yaDioLike = yoDiLikePost,
                                onOk = { nuevoTotal, nuevoLike ->
                                    likesPost = nuevoTotal
                                    yoDiLikePost = nuevoLike
                                    onLikeChanged(post.id, nuevoLike)
                                },
                                onError = { error ->
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    ) {
                        Icon(
                            imageVector = if (yoDiLikePost) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (yoDiLikePost) Color(0xFFE91E63) else Color.Gray
                        )
                    }
                    Text(
                        text = "$likesPost",
                        fontSize = 13.sp,
                        color = if (yoDiLikePost) Color(0xFFE91E63) else Color.Gray,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Botón respuestas con icono
                Row(
                    modifier = Modifier.clickable { expandido = !expandido },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "💬 ${if (comentariosCargados) comentarios.size else "..."}",
                        color = PurpleBtn,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = if (expandido) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (expandido) "Ocultar" else "Ver respuestas",
                        tint = PurpleBtn
                    )
                }
            }

            // Sección expandible de comentarios
            AnimatedVisibility(
                visible = expandido,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (!comentariosCargados) {
                        Text(text = "Cargando respuestas...", color = Color.Gray, fontSize = 13.sp)
                    } else if (comentarios.isEmpty()) {
                        Text(text = "Aún no hay respuestas. ¡Sé el primero!", color = Color.Gray, fontSize = 13.sp)
                    } else {
                        comentarios.forEach { comentario ->
                            ComentarioCardInterna(comentario = comentario)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = nuevoComentario,
                            onValueChange = { if (it.length <= 300) nuevoComentario = it },
                            label = { Text("Responder... (${nuevoComentario.length}/300)") },
                            modifier = Modifier.weight(1f),
                            minLines = 1,
                            maxLines = 4,
                            singleLine = false
                        )

                        IconButton(
                            onClick = {
                                if (nuevoComentario.isBlank() || enviando) return@IconButton
                                enviando = true
                                comentarioRepository.agregarComentario(
                                    postId = post.id,
                                    texto = nuevoComentario.trim(),
                                    authorName = nombre,
                                    authorRole = rol,
                                    onOk = {
                                        comentarios.add(
                                            Comment(
                                                postId = post.id,
                                                authorName = nombre,
                                                authorRole = rol,
                                                content = nuevoComentario.trim(),
                                                createdAt = System.currentTimeMillis()
                                            )
                                        )
                                        nuevoComentario = ""
                                        enviando = false
                                    },
                                    onError = { error ->
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                        enviando = false
                                    }
                                )
                            },
                            modifier = Modifier.padding(bottom = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Send,
                                contentDescription = "Enviar",
                                tint = if (nuevoComentario.isBlank()) Color.Gray else PurpleBtn
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ComentarioCardInterna(comentario: Comment) {
    val isAdmin = comentario.authorRole.lowercase() == "admin"

    var likesComentario by remember { mutableStateOf(0) }
    var yoDiLike by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isAdmin) PurpleBtn.copy(alpha = 0.08f) else Color.White.copy(alpha = 0.6f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Fila 1: nombre + fecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isAdmin) "👩‍⚕️ ${comentario.authorName}" else comentario.authorName,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = if (isAdmin) PurpleBtn else PurpleBlueText
                    )
                    if (isAdmin) {
                        Text(
                            text = "ADMIN",
                            style = MaterialTheme.typography.labelSmall,
                            color = PurpleBtn
                        )
                    }
                }
                Text(
                    text = formatearFecha(comentario.createdAt),
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Fila 2: texto de la respuesta
            Text(
                text = comentario.content,
                style = MaterialTheme.typography.bodySmall,
                color = PurpleBlueText
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Fila 3: like
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = {
                        if (yoDiLike) {
                            likesComentario--
                            yoDiLike = false
                        } else {
                            likesComentario++
                            yoDiLike = true
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (yoDiLike) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Like comentario",
                        tint = if (yoDiLike) Color(0xFFE91E63) else Color.Gray
                    )
                }
                Text(
                    text = "$likesComentario",
                    fontSize = 12.sp,
                    color = if (yoDiLike) Color(0xFFE91E63) else Color.Gray
                )
            }
        }
    }
}
// ─────────────────────────────────────────
// Componentes reutilizables de configuración
// ─────────────────────────────────────────

@Composable
fun ConfigCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun SeccionHeader(
    titulo: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = titulo,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = PurpleBlueText,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}

@Composable
fun FilaSwitch(
    titulo: String,
    descripcion: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = PurpleBlueText
            )
            Text(
                text = descripcion,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        androidx.compose.material3.Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = androidx.compose.material3.SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PurpleBtn
            )
        )
    }
}

@Composable
fun FilaClickable(
    titulo: String,
    descripcion: String,
    tituloColor: Color = PurpleBlueText,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = tituloColor
            )
            Text(
                text = descripcion,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}