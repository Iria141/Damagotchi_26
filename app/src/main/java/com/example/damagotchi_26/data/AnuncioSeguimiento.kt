package com.example.damagotchi_26.data

import androidx.compose.ui.graphics.Color

data class AnuncioSeguimiento(
    val id: String = "",
    val titulo: String = "",
    val semanaGestacion: Int = 1,
    val categoria: String = "",
    val contenido: String = "",
    val fuente: String = "",
    val urlFuente: String = "",
    val autorUid: String = "",
    val imagenUrl: String = "",
    val fechaCreacion: Long = System.currentTimeMillis()
)


