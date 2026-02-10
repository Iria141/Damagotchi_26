package com.example.damagotchi_26.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.damagotchi_26.domain.MomentoDia
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

import kotlinx.coroutines.launch

class TransicionViewModel : ViewModel() {

    //Esclarifica si es dia o noche
    var _momentoDia = MutableStateFlow(MomentoDia.DIA)
    val momentoDia: StateFlow<MomentoDia> = _momentoDia

    // calcula el dia y la semana
    private val _diaActual = MutableStateFlow(1)
    val diaActual: StateFlow<Int> = _diaActual

    val semanaActual: StateFlow<Int> =
        diaActual.map { dia ->
            ((dia - 1) / 7) + 1
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 1
        )

    private var trabajoLuz: Job? = null

    fun iniciarCicloLuz() {
        if (trabajoLuz != null) return

        trabajoLuz = viewModelScope.launch {
            while (true) {
                delay(tiempoLuz()) // 1,24 horas
                _momentoDia.value =
                    if (_momentoDia.value == MomentoDia.DIA) MomentoDia.NOCHE
                else MomentoDia.DIA

                // cuando vuelve a DIA, pasa un día
                if (_momentoDia.value == MomentoDia.DIA) {
                    avanzarDia()
                    }
            }
        }
    }

    private fun avanzarDia() {
        _diaActual.value += 1
    }

    private fun tiempoLuz(): Long =
        if (TimeConfig.DEBUG)
            TimeConfig.CICLO_LUZ_DEBUG
        else
            TimeConfig.CICLO_LUZ_REAL

    override fun onCleared() {
        trabajoLuz?.cancel()
        super.onCleared()
    }

}

object TimeConfig { //MODO PRESENTACION -- ACTIVAR DEBUG

    const val DEBUG = true // false en producción

    const val SEGUNDO = 1_000L
    const val MINUTO = 60 * SEGUNDO
    const val HORA = 60 * MINUTO

    const val CICLO_LUZ_DEBUG = 5 * SEGUNDO        // 5 segundos
    const val CICLO_LUZ_REAL = (1.24 * HORA).toLong()
}

