package com.example.damagotchi_26.ui.rooms

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.damagotchi_26.R
import com.example.damagotchi_26.domain.Pet
import com.example.damagotchi_26.ui.components.IconsPanel
import com.example.damagotchi_26.ui.theme.ActionButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BedRoom(
    pet: Pet,
    dormir: () -> Unit,

) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Dormitorio",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 30.sp
                        )

                        Text(
                            text = "Descanso: ${pet.descanso}%"
                            ,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Fondo
            Image(
                painter = painterResource(id = R.drawable.dormitorio),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            //Iconos izquierda
            IconsPanel(pet = pet)
            Spacer(Modifier.width(12.dp))

            // 🔽 Acciones abajo
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(20.dp)
                    .zIndex(10f),
                horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionButton(
                    image = R.drawable.dormido,
                    text = "" +
                            "Dormir",
                    onClick = dormir
                )
            }
        }
    }
}


