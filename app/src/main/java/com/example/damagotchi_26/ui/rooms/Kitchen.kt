package com.example.damagotchi_26.ui.rooms

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.damagotchi_26.R
import com.example.damagotchi_26.domain.Pet
import com.example.damagotchi_26.ui.components.IconsPanel
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Kitchen(
    pet: Pet,
    comer: () -> Unit,
    beber: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Cocina",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Hambre actual: ${pet.hambre}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Sed actual: ${pet.sed}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )

        }
    ){ padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ){

            Image(
                painter = painterResource(id = R.drawable.cocina),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )

        Row(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Panel de iconos
            IconsPanel(pet = pet)
            Spacer(Modifier.width(12.dp))



            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                // Personaje centrado
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Green),
                    contentAlignment = Alignment.Center

                ){
                Text(
                    text = if (pet.estaMal()) "🐣💧" else "🐣",
                    style = MaterialTheme.typography.displayLarge
                )
                }

                //botones con acciones
                Row(
                    horizontalArrangement = Arrangement.spacedBy(48.dp)
                ) {
                    KitchenActionButton(
                        icon = "🍎",
                        text = "Comer",
                        onClick = comer
                    )

                    KitchenActionButton(
                        icon = "💧",
                        text = "Beber",
                        onClick = beber
                    )
                }
            }
        }
        }
    }
}

@Composable
fun KitchenActionButton(
    icon: String,
    text: String,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(72.dp)
                .background(
                    MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Text(icon, style = MaterialTheme.typography.headlineMedium)
        }
        Spacer(Modifier.height(6.dp))
        Text(text, style = MaterialTheme.typography.labelMedium)
    }
}


