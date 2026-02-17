package com.example.damagotchi_26.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.damagotchi_26.data.PetPrefs
import com.example.damagotchi_26.domain.MomentoDia
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

import kotlinx.coroutines.launch

class TransicionViewModel (  private val petPrefs: PetPrefs) : ViewModel() {

    //Esclarifica si es dia o noche
    var _momentoDia = MutableStateFlow(MomentoDia.DIA)
    val momentoDia: StateFlow<MomentoDia> = _momentoDia

    // calcula el dia y la semana
    private val _diaActual = MutableStateFlow(1)
    val diaActual: StateFlow<Int> = _diaActual

    private val _avisos = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val avisos = _avisos.asSharedFlow()

    val semanaActual: StateFlow<Int> =
        diaActual.map { dia ->
            ((dia - 1) / 7) + 1
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 1
        )

    private var trabajoLuz: Job? = null
    private var ultimaSemanaAvisada: Int? = null


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

    fun comprobarAvisoTrimestre(semana: Int) {
        viewModelScope.launch {
            val (trimestre, umbral) = when (semana) {
                14 -> com.example.damagotchi_26.domain.Trimestre.SEGUNDO to 14
                28 -> com.example.damagotchi_26.domain.Trimestre.TERCERO to 28
                1  -> com.example.damagotchi_26.domain.Trimestre.PRIMERO to 1  // opcional
                else -> return@launch
            }

            // ✅ si ya se mostró antes (aunque cierres la app), no se repite
            if (petPrefs.yaMostradoAviso(umbral)) return@launch

            val texto = com.example.damagotchi_26.ui.theme.mensajesDeInicio(trimestre)
                .firstOrNull() ?: return@launch

            _avisos.emit(texto)
            petPrefs.marcarAvisoMostrado(umbral)
        }
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

