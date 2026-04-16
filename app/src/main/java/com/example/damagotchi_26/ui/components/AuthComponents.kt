package com.example.damagotchi_26.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.damagotchi_26.ui.Color.Color.BorderGray
import com.example.damagotchi_26.ui.Color.Color.CardGray
import com.example.damagotchi_26.ui.Color.Color.PurpleBlueText
import com.example.damagotchi_26.ui.Color.Color.PurpleBtn
import com.example.damagotchi_26.data.Post
import com.example.damagotchi_26.ui.Color.Color.YellowStar


@Composable
fun AuthBackground(
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp)
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
                focusedContainerColor = CardGray,
                unfocusedContainerColor = CardGray,
                disabledContainerColor = CardGray,

                focusedBorderColor = BorderGray,
                unfocusedBorderColor = BorderGray,
                disabledBorderColor = BorderGray,

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
        colors = CardDefaults.cardColors(containerColor = CardGray),
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



