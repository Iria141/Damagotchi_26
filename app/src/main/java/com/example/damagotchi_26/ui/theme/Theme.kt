package com.example.damagotchi_26.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

//Theme es un archivo que contiene una función composable, no es una clase normal.

private val LightColors = lightColorScheme()

@Composable
fun Damagotchi_26Theme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
