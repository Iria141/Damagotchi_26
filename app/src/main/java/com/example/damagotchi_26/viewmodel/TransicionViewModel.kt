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

class TransicionViewModel (
    private val petPrefs: PetPrefs?= null
) : ViewModel() {

    //Esclarifica si es dia o noche
    var _momentoDia = MutableStateFlow(MomentoDia.DIA)
    val momentoDia: StateFlow<MomentoDia> = _momentoDia

    // calcula el dia y la semana
    private val _diaActual = MutableStateFlow(1)
    val diaActual: StateFlow<Int> = _diaActual

    private val _avisos = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val avisos = _avisos.asSharedFlow()

    // Sistema de pérdida
    private val _perdidaEmbarazo = MutableStateFlow(false)
    val perdidaEmbarazo: StateFlow<Boolean> = _perdidaEmbarazo

    private val _perdidaDefinitiva = MutableStateFlow(false)
    val perdidaDefinitiva: StateFlow<Boolean> = _perdidaDefinitiva

    private var diasCriticosConsecutivos = 0
    private var vecesPerdida = 0

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

    init {
        iniciarCicloLuz()
    }


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

    fun comprobarPerdida(
        energia: Int,
        hambre: Int,
        sed: Int,
        limpieza: Int,
        actividad: Int,
        descanso: Int
    ) {
        val medidoresCriticos = listOf(energia, hambre, sed, limpieza, actividad, descanso)
            .count { it <= 20 }

        if (medidoresCriticos >= 3) {
            diasCriticosConsecutivos++
            if (diasCriticosConsecutivos >= 2) {
                vecesPerdida++
                diasCriticosConsecutivos = 0
                if (vecesPerdida >= 2) {
                    // Segunda pérdida → definitiva
                    _perdidaDefinitiva.value = true
                } else {
                    // Primera pérdida → aviso
                    _perdidaEmbarazo.value = true
                }
            }
        } else {
            diasCriticosConsecutivos = 0
        }
    }

    fun resetearPerdida() {
        _perdidaEmbarazo.value = false
        diasCriticosConsecutivos = 0
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

    fun comprobarAvisoTrimestre(semana: Int, rol: String) {
        val prefs = petPrefs ?: return
        viewModelScope.launch {
            val (trimestre, umbral) = when (semana) {
                14 -> com.example.damagotchi_26.domain.Trimestre.SEGUNDO to 14
                28 -> com.example.damagotchi_26.domain.Trimestre.TERCERO to 28
                1 -> com.example.damagotchi_26.domain.Trimestre.PRIMERO to 1  // opcional
                else -> return@launch
            }

            // si ya se mostró antes (aunque cierres la app), no se repite
            if (prefs.yaMostradoAviso(umbral)) return@launch

            val texto = com.example.damagotchi_26.ui.theme.mensajesDeInicio(trimestre, rol)
                .firstOrNull() ?: return@launch

            _avisos.emit(texto)
            prefs.marcarAvisoMostrado(umbral)
        }
    }


    fun comprobarEventoEspecial(semana: Int, rol: String) {
        val prefs = petPrefs ?: return

        viewModelScope.launch {
            val texto = com.example.damagotchi_26.ui.theme
                .mensajeEventoEspecial(semana, rol) ?: return@launch

            if (prefs.yaMostradoEvento(semana)) return@launch

            _avisos.emit(texto)
            prefs.marcarEventoMostrado(semana)
        }
    }

    fun comprobarConsejoDelDia(semana: Int, dia: Int, rol: String) {
        val prefs = petPrefs ?: return

        viewModelScope.launch {
            val ultimoDiaMostrado = prefs.ultimoDiaConsejoMostrado()
            if (ultimoDiaMostrado == dia) return@launch

            val trimestre = com.example.damagotchi_26.ui.theme.trimestreDeSemana(semana)

            val texto = com.example.damagotchi_26.ui.theme
                .consejoDelDia(trimestre, rol, dia) ?: return@launch

            _avisos.emit("💡 Consejo del día: $texto")
            prefs.marcarConsejoDia(dia)
        }
    }

    fun resetearTodo() {
        _perdidaEmbarazo.value = false
        _perdidaDefinitiva.value = false
        diasCriticosConsecutivos = 0
        vecesPerdida = 0
        ultimaSemanaAvisada = null
        _diaActual.value = 1
        _momentoDia.value = MomentoDia.DIA

        // Resetea los avisos de trimestre en PetPrefs
        viewModelScope.launch {
            petPrefs?.resetearAvisos()
        }
    }


}

