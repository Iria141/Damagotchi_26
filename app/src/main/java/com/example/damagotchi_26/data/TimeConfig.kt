object TimeConfig {
    const val DEBUG = true // true para pruebas, false en producción

    const val SEGUNDO = 1_000L
    const val MINUTO = 60 * SEGUNDO
    const val HORA = 60 * MINUTO

    const val CICLO_LUZ_DEBUG = 5 * SEGUNDO
    const val CICLO_LUZ_REAL = (1.24 * HORA).toLong()

    // Duración de un día ficticio
    const val DIA_DEBUG = 1 * MINUTO       // 1 minuto en modo debug
    const val DIA_REAL = 155 * MINUTO      // 155 minutos en producción

    val DIA get() = if (DEBUG) DIA_DEBUG else DIA_REAL
}