package com.example.damagotchi_26.domain

data class Pet(
    val energia: Int = 100,
    val hambre: Int = 100,
    val sed: Int = 100,
    val limpieza: Int = 100,
    val actividad: Int = 100,
    val descanso : Int = 100,

    val semanaEmbarazo: Int = 1, //1..40,
    val diaEmbarazo: Int = 1,

    val sumaEnergia: Int = 0,
    val sumaHambre: Int = 0,
    val sumaSed: Int = 0,
    val sumaLimpieza: Int = 0,
    val sumaActividad: Int = 0,
    val sumaDescanso: Int = 0,
    val diasEvaluados: Int = 0


)

    fun limitar(valor: Int): Int = valor.coerceIn(0, 100) //limita la barra de estado entre 0 y 100

