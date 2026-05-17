package com.example.damagotchi_26.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.damagotchi_26.data.PetPrefs
import com.example.damagotchi_26.domain.Pet
import com.example.damagotchi_26.domain.limitar
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PetViewModel(
    private val prefs: PetPrefs
) : ViewModel() {

    val pet: StateFlow<Pet> = prefs.petFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(3_000),
        initialValue = Pet()
    )

    private var trabajoTick: Job? = null

    // Tick de degradación de medidores
    // Se ejecuta cada 30s y baja los medidores gradualmente

    fun iniciarTick() {
        if (trabajoTick != null) return
        trabajoTick = viewModelScope.launch {
            while (true) {
                delay(30_000)
                val actual = pet.value
                prefs.guardar(
                    actual.copy(
                        hambre    = limitar(actual.hambre    - 2),
                        energia   = limitar(actual.energia   - 1),
                        actividad = limitar(actual.actividad - 2),
                        sed       = limitar(actual.sed       - 2),
                        limpieza  = limitar(actual.limpieza  - 1),
                        descanso  = limitar(actual.descanso  - 2)
                    )
                )
            }
        }
    }

    // Sincronización de tiempo real al abrir la app
    // Calcula cuántos días han pasado desde fechaInicioJuego y actualiza el estado

    fun sincronizarTiempoReal() {
        viewModelScope.launch {
            var fechaInicio = prefs.obtenerFechaInicio()

            // Primera vez que se inicia el juego
            if (fechaInicio == 0L) {
                fechaInicio = System.currentTimeMillis()
                prefs.guardarFechaInicio(fechaInicio)
                return@launch
            }

            val duracionDia = TimeConfig.DIA
            val ahora = System.currentTimeMillis()
            val diasTranscurridos = ((ahora - fechaInicio) / duracionDia).toInt() + 1
            val diaReal = diasTranscurridos.coerceIn(1, 280)
            val semanaReal = (((diaReal - 1) / 7) + 1).coerceAtMost(40)

            val actual = pet.value

            // Solo actualizar si el día calculado es mayor al guardado
            if (diaReal > actual.diaEmbarazo) {
                val diasNuevos = diaReal - actual.diaEmbarazo
                prefs.guardar(
                    actual.copy(
                        diaEmbarazo    = diaReal,
                        semanaEmbarazo = semanaReal,
                        sumaEnergia    = actual.sumaEnergia  + actual.energia   * diasNuevos,
                        sumaHambre     = actual.sumaHambre   + actual.hambre    * diasNuevos,
                        sumaSed        = actual.sumaSed      + actual.sed       * diasNuevos,
                        sumaLimpieza   = actual.sumaLimpieza + actual.limpieza  * diasNuevos,
                        sumaActividad  = actual.sumaActividad+ actual.actividad * diasNuevos,
                        sumaDescanso   = actual.sumaDescanso + actual.descanso  * diasNuevos,
                        diasEvaluados  = actual.diasEvaluados + diasNuevos
                    )
                )
            }
        }
    }

    // ── Acciones del juego ────────────────────────────────────────────────────

    fun alimentar() = actualizar { actual ->
        actual.copy(
            hambre    = limitar(actual.hambre    + 25),
            energia   = limitar(actual.energia   + 15),
            limpieza  = limitar(actual.limpieza  - 5),
            actividad = limitar(actual.actividad - 3),
            descanso  = limitar(actual.descanso  - 3)
        )
    }

    fun hidratar() = actualizar { actual ->
        actual.copy(
            sed     = limitar(actual.sed     + 20),
            hambre  = limitar(actual.hambre  + 5),
            energia = limitar(actual.energia + 10),
            limpieza = limitar(actual.limpieza - 3)
        )
    }

    fun prepararComida() = actualizar { actual ->
        actual.copy(
            energia   = limitar(actual.energia   + 10),
            actividad = limitar(actual.actividad + 8),
            hambre    = limitar(actual.hambre    - 5),
            sed       = limitar(actual.sed       - 3)
        )
    }

    fun dormir() = actualizar { actual ->
        actual.copy(
            descanso = limitar(actual.descanso + 45),
            energia  = limitar(actual.energia  + 30),
            hambre   = limitar(actual.hambre   - 8),
            sed      = limitar(actual.sed      - 6)
        )
    }

    fun siesta() = actualizar { actual ->
        actual.copy(
            descanso = limitar(actual.descanso + 25),
            energia  = limitar(actual.energia  + 15),
            hambre   = limitar(actual.hambre   - 3),
            sed      = limitar(actual.sed      - 2)
        )
    }

    fun bano() = actualizar { actual ->
        actual.copy(
            energia  = limitar(actual.energia  + 15),
            hambre   = limitar(actual.hambre   - 3),
            limpieza = limitar(actual.limpieza + 20),
            sed      = limitar(actual.sed      - 3)
        )
    }

    fun ducharse() = actualizar { actual ->
        actual.copy(
            limpieza = limitar(actual.limpieza + 20),
            energia  = limitar(actual.energia  + 15),
            sed      = limitar(actual.sed      - 3)
        )
    }

    fun lavarDientes() = actualizar { actual ->
        actual.copy(limpieza = limitar(actual.limpieza + 12))
    }

    fun cuidarPiel() = actualizar { actual ->
        actual.copy(
            limpieza = limitar(actual.limpieza + 15),
            energia  = limitar(actual.energia  + 3)
        )
    }

    fun caminar() = actualizar { actual ->
        actual.copy(
            energia   = limitar(actual.energia   - 12),
            hambre    = limitar(actual.hambre    - 8),
            limpieza  = limitar(actual.limpieza  - 10),
            sed       = limitar(actual.sed       - 10),
            actividad = limitar(actual.actividad + 25)
        )
    }

    fun estirar() = actualizar { actual ->
        actual.copy(
            energia   = limitar(actual.energia   - 5),
            hambre    = limitar(actual.hambre    - 5),
            sed       = limitar(actual.sed       - 5),
            actividad = limitar(actual.actividad + 15)
        )
    }

    fun tocarPiano() = actualizar { actual ->
        actual.copy(
            actividad = limitar(actual.actividad + 12),
            descanso  = limitar(actual.descanso  + 10),
            energia   = limitar(actual.energia   - 5)
        )
    }

    fun jugarPelota() = actualizar { actual ->
        actual.copy(
            actividad = limitar(actual.actividad + 15),
            descanso  = limitar(actual.descanso  - 8),
            energia   = limitar(actual.energia   - 10)
        )
    }

    fun pintar() = actualizar { actual ->
        actual.copy(
            actividad = limitar(actual.actividad + 10),
            descanso  = limitar(actual.descanso  + 10),
            energia   = limitar(actual.energia   + 5)
        )
    }

    fun resetearMedidores() = actualizar { actual ->
        actual.copy(
            energia   = 50,
            hambre    = 50,
            sed       = 50,
            limpieza  = 50,
            actividad = 50,
            descanso  = 50
        )
    }

    fun acumularDia() = actualizar { actual ->
        actual.copy(
            sumaEnergia   = actual.sumaEnergia   + actual.energia,
            sumaHambre    = actual.sumaHambre    + actual.hambre,
            sumaSed       = actual.sumaSed       + actual.sed,
            sumaLimpieza  = actual.sumaLimpieza  + actual.limpieza,
            sumaActividad = actual.sumaActividad + actual.actividad,
            sumaDescanso  = actual.sumaDescanso  + actual.descanso,
            diasEvaluados = actual.diasEvaluados + 1
        )
    }

    fun resetearJuego() {
        viewModelScope.launch {
            prefs.guardar(
                Pet(
                    energia       = 100, hambre    = 100, sed          = 100,
                    limpieza      = 100, actividad = 100, descanso     = 100,
                    semanaEmbarazo = 1,  diaEmbarazo = 1,
                    sumaEnergia   = 0,   sumaHambre  = 0, sumaSed      = 0,
                    sumaLimpieza  = 0,   sumaActividad = 0, sumaDescanso = 0,
                    diasEvaluados = 0
                )
            )
            prefs.resetearFechaInicio()
        }
    }

    private fun actualizar(bloque: (Pet) -> Pet) {
        viewModelScope.launch {
            prefs.guardar(bloque(pet.value))
        }
    }

    override fun onCleared() {
        trabajoTick?.cancel()
        super.onCleared()
    }
}