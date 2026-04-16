package com.example.damagotchi_26.utils

fun calcularDiaYSemana(fechaUltimaRegla: Long): Pair<Int, Int> {
    if (fechaUltimaRegla == 0L) return Pair(0, 0)

    val hoy = System.currentTimeMillis()
    val dias = ((hoy - fechaUltimaRegla) / (1000L * 60 * 60 * 24)).toInt() + 1
    val semana = ((dias - 1) / 7) + 1

    return Pair(dias, semana)
}