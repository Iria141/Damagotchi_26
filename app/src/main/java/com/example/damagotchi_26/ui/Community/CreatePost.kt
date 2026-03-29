package com.example.damagotchi_26.ui.Community

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
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
    isAdmin: Boolean = false,
    onPublishClick: (String, String, String) -> Unit = { _, _, _ -> },
    onBack: () -> Unit = {}
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("pregunta") }

    val availableTypes = if (isAdmin) {
        listOf("pregunta", "opinion", "anuncio")
    } else {
        listOf("pregunta", "opinion")
    }
    val maxContenido = 250;
    val maxTitulo = 50;

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
                    text = "Comparte una duda, una opinión o un anuncio con la comunidad",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                AuthCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AuthTextField(
                        value = title,
                        onValueChange = {
                            if (it.length <= maxTitulo) {
                                title = it
                            }
                        },
                        label = "Título"
                    )

                    AuthTextField(
                        value = content,
                        onValueChange = {
                            if (it.length <= maxContenido) {
                                content = it
                            }
                        },
                        label = "Contenido",
                        singleLine = false,
                        modifier = Modifier
                            .height(200.dp)
                    )

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
                            TypeOptionChip(
                                text = type,
                                selected = selectedType == type,
                                onClick = { selectedType = type },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    PrimaryAuthButton(
                        text = "Publicar",
                        onClick = {
                            onPublishClick(title, content, selectedType)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 35.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun TypeOptionChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) PurpleBtn else CardGray
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text.replaceFirstChar { it.uppercase() },
                color = if (selected) Color.White else PurpleBlueText,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreatePostScreenPreview() {
    MaterialTheme {
        CreatePostScreen(isAdmin = true)
    }
}