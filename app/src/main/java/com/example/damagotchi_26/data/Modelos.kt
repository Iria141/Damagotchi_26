package com.example.damagotchi_26.data

data class AvisoTrimestre(
    val semana: Int,
    val titulo: String,
    val mensaje: String
)

data class EventoEspecial(
    val semana: Int,
    val titulo: String,
    val textoMadre: String,
    val textoPadre: String,
    val textoOtro: String,
    val imageRes: Int? = null
)

fun EventoEspecial.textoParaRol(rol: String): String = when (rol) {
    "Madre" -> textoMadre
    "Padre" -> textoPadre
    else    -> textoOtro
}