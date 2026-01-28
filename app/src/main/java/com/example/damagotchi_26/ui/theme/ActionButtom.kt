package com.example.damagotchi_26.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

//Clase que define el estilo de los botonws inferiores, botones de acciones

@Composable
fun ActionButton(
    image: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .size(75.dp),
        shape = RoundedCornerShape(30.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 2.dp,
        shadowElevation = 3.dp,
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )

            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}