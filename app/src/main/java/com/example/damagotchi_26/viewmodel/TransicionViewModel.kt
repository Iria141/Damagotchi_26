package com.example.damagotchi_26.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.damagotchi_26.data.EventoEspecial
import com.example.damagotchi_26.data.PetPrefs
import com.example.damagotchi_26.domain.MomentoDia
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AvisoTrimestre(
    val semana: Int,
    val titulo: String,
    val mensaje: String
)

class TransicionViewModel(
    private val petPrefs: PetPrefs? = null
) : ViewModel() {

    var _momentoDia = MutableStateFlow(MomentoDia.DIA)
    val momentoDia: StateFlow<MomentoDia> = _momentoDia

    private val _diaActual = MutableStateFlow(1)
    val diaActual: StateFlow<Int> = _diaActual

    val semanaActual: StateFlow<Int> =
        diaActual.map { dia -> ((dia - 1) / 7) + 1 }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 1
            )

    private val _avisoTrimestre = MutableStateFlow<AvisoTrimestre?>(null)
    val avisoTrimestre: StateFlow<AvisoTrimestre?> = _avisoTrimestre.asStateFlow()

    private val _eventoConImagen =
        MutableStateFlow<com.example.damagotchi_26.data.EventoEspecial?>(null)
    val eventoConImagen: StateFlow<com.example.damagotchi_26.data.EventoEspecial?> =
        _eventoConImagen.asStateFlow()

    private val _perdidaEmbarazo = MutableStateFlow(false)
    val perdidaEmbarazo: StateFlow<Boolean> = _perdidaEmbarazo

    private val _perdidaDefinitiva = MutableStateFlow(false)
    val perdidaDefinitiva: StateFlow<Boolean> = _perdidaDefinitiva

    private var diasCriticosConsecutivos = 0
    private var vecesPerdida = 0

    private var trabajoLuz: Job? = null

    init {
        iniciarCicloLuz()
    }


    fun iniciarCicloLuz() {
        if (trabajoLuz != null) return
        trabajoLuz = viewModelScope.launch {
            while (true) {
                delay(tiempoLuz())
                _momentoDia.value =
                    if (_momentoDia.value == MomentoDia.DIA) MomentoDia.NOCHE
                    else MomentoDia.DIA
                if (_momentoDia.value == MomentoDia.DIA) avanzarDia()
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
                    _perdidaDefinitiva.value = true
                } else {
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


    fun comprobarAvisoTrimestre(semana: Int, rol: String) {
        val prefs = petPrefs ?: return
        viewModelScope.launch {
            val (trimestre, umbral) = when (semana) {
                1  -> com.example.damagotchi_26.domain.Trimestre.PRIMERO to 1
                13 -> com.example.damagotchi_26.domain.Trimestre.SEGUNDO to 13
                28 -> com.example.damagotchi_26.domain.Trimestre.TERCERO to 28
                else -> return@launch
            }

            if (prefs.yaMostradoAviso(umbral)) return@launch

            val texto = com.example.damagotchi_26.ui.theme
                .mensajesDeInicio(trimestre, rol)
                .firstOrNull() ?: return@launch

            val titulo = when (semana) {
                1  -> "¡Bienvenida al juego!"
                13 -> "Segundo trimestre"
                28 -> "Tercer trimestre"
                else -> "Nuevo trimestre"
            }

            _avisoTrimestre.value = AvisoTrimestre(
                semana  = semana,
                titulo  = titulo,
                mensaje = texto
            )
            prefs.marcarAvisoMostrado(umbral)
        }
    }

    fun descartarAvisoTrimestre() {
        _avisoTrimestre.value = null
    }


    fun comprobarEventoEspecial(semana: Int, rol: String) {
        val prefs = petPrefs ?: return
        viewModelScope.launch {
            if (prefs.yaMostradoEvento(semana)) return@launch

            val evento = com.example.damagotchi_26.ui.theme.EVENTOS_ESPECIALES
                .firstOrNull { it.semana == semana } ?: return@launch

            _eventoConImagen.value = evento
        }
    }

    fun descartarEventoImagen(semana: Int) {
        viewModelScope.launch {
            petPrefs?.marcarEventoMostrado(semana)
            _eventoConImagen.value = null
        }
    }




    fun resetearTodo() {
        _perdidaEmbarazo.value = false
        _perdidaDefinitiva.value = false
        _avisoTrimestre.value = null
        _eventoConImagen.value = null
        diasCriticosConsecutivos = 0
        vecesPerdida = 0
        _diaActual.value = 1
        _momentoDia.value = MomentoDia.DIA
        viewModelScope.launch { petPrefs?.resetearAvisos() }
    }

    private fun tiempoLuz(): Long =
        if (TimeConfig.DEBUG) TimeConfig.CICLO_LUZ_DEBUG
        else TimeConfig.CICLO_LUZ_REAL

    override fun onCleared() {
        trabajoLuz?.cancel()
        super.onCleared()
    }
}