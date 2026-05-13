package com.example.damagotchi_26.data

object TimeConfig { //MODO PRESENTACION -- ACTIVAR DEBUG
    const val DEBUG = false // false en producción
    const val SEGUNDO = 1_000L
    const val MINUTO = 60 * SEGUNDO
    const val HORA = 60 * MINUTO

    const val CICLO_LUZ_DEBUG = 5 * SEGUNDO        // 5 segundos
    const val CICLO_LUZ_REAL = (1.24 * HORA).toLong()
}