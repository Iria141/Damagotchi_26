package com.example.damagotchi_26.viewmodel
object TimeConfig {
    const val DEBUG = false
    const val TESTING = true
    const val SEGUNDO = 1_000L
    const val MINUTO  = 60 * SEGUNDO
    const val HORA    = 60 * MINUTO

    const val CICLO_LUZ_DEBUG   = 5 * SEGUNDO
    const val CICLO_LUZ_TESTING = 30 * SEGUNDO    // 30 segundos para probar
    const val CICLO_LUZ_REAL    = (1.24 * HORA).toLong()

    const val DIA_DEBUG   = 1 * MINUTO
    const val DIA_TESTING = 5 * MINUTO            // 5 minutos por día para probar
    const val DIA_REAL    = 155 * MINUTO

    val CICLO_LUZ get() = when {
        DEBUG   -> CICLO_LUZ_DEBUG
        TESTING -> CICLO_LUZ_TESTING
        else    -> CICLO_LUZ_REAL
    }

    val DIA get() = when {
        DEBUG   -> DIA_DEBUG
        TESTING -> DIA_TESTING
        else    -> DIA_REAL
    }
}