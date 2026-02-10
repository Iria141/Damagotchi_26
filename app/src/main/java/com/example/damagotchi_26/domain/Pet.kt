package com.example.damagotchi_26.domain

data class Pet(
    val energia: Int = 100,
    val hambre: Int = 100,
    val sed: Int = 100,
    val limpieza: Int = 100,
    val actividad: Int = 100,
    val descanso : Int = 100,

    val semanaEmbarazo: Int = 1, //1..200
    val diaEmbarazo: Int = 1 //1..40


) {
    fun estaMal(): Boolean =
        hambre <= 20 || energia <= 20 || actividad <= 20 || sed <= 20 || descanso <= 20 || limpieza <= 20

    fun estaKO(): Boolean =
        hambre == 0 || energia == 0 || actividad == 0 || sed == 0 || descanso == 0 || limpieza == 0

}

    fun limitar(valor: Int): Int = valor.coerceIn(0, 100) //limita la barra de estado entre 0 y 100
