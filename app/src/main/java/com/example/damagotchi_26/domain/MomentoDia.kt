package com.example.damagotchi_26.domain

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime

enum class MomentoDia { DIA, NOCHE }

@RequiresApi(Build.VERSION_CODES.O)
fun obtenerMomentoDia(): MomentoDia {
    val hora = LocalTime.now().hour

    return when (hora) {
        in 6..11 -> MomentoDia.DIA
        else -> MomentoDia.NOCHE
    }
}
