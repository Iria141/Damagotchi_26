package com.example.damagotchi_26.ui.rooms

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.damagotchi_26.R
import com.example.damagotchi_26.domain.Pet
import com.example.damagotchi_26.ui.components.IconsPanel
import com.example.damagotchi_26.ui.theme.ActionButton

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
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Cocina",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 30.sp
                        )

                        Text(
                            text = "Hambre: ${pet.hambre}%    " +
                                    "|   Sed: ${pet.sed}%   "+
                                    "|   Energía: ${pet.energia}%"
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
                painter = painterResource(id = R.drawable.cocina),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

                //Iconos
                IconsPanel(pet = pet)
                Spacer(Modifier.width(12.dp))

                // 🔽 Acciones abajo
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(16.dp)
                        .zIndex(10f),
                    horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ActionButton(
                        image = R.drawable.salteado,
                        text ="Comer",
                        onClick = comer,
                    )
                    ActionButton(
                        image = R.drawable.botella_agua,
                        text = "Beber",
                        onClick = beber,
                    )
                }
            }
        }
    }


//PREVISUALIZACION

@Preview(showBackground = true)
@Composable
fun KitchenPreviewSimple() {
    Kitchen(
        pet = Pet(),   // todo a 100 por defecto
        comer = {},
        beber = {}
    )
}

