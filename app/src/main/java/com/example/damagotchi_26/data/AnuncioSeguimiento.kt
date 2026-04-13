package com.example.damagotchi_26.data

import android.R

data class AnuncioSeguimiento(
    val titulo: String = "",
    val semanaGestacion:  Int = 1,
    val categoria: String = "",
    val contenido: String = "",
    val fuente: String = "",
    val urlFuente: String = "",
    val autorUid: String = "",
    val fechaCreacion: Long = System.currentTimeMillis()
)