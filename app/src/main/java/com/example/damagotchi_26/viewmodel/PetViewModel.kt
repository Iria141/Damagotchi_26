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
    private var trabajoTiempo: Job? = null

    private val duracionDia = TimeConfig.DIA

    fun iniciarTick() {
        if (trabajoTick != null) return

        trabajoTick = viewModelScope.launch {
            while (true) {
                delay(30_000)
                val actual = pet.value
                val actualizado = actual.copy(
                    hambre = limitar(actual.hambre - 2),
                    energia = limitar(actual.energia - 1),
                    actividad = limitar(actual.actividad - 2),
                    sed = limitar(actual.sed - 2),
                    limpieza = limitar(actual.limpieza - 1),
                    descanso = limitar(actual.descanso - 2)
                )
                prefs.guardar(actualizado)
            }
        }
    }

    fun alimentar() = actualizar { actual ->
        actual.copy(
            hambre = limitar(actual.hambre + 25),
            energia = limitar(actual.energia + 15),
            limpieza = limitar(actual.limpieza - 5),
            actividad = limitar(actual.actividad - 3),
            descanso = limitar(actual.descanso - 3)
        )
    }

    fun hidratar() = actualizar { actual ->
        actual.copy(
            sed = limitar(actual.sed + 20),
            hambre = limitar(actual.hambre + 5),
            energia = limitar(actual.energia + 10),
            limpieza = limitar(actual.limpieza - 3)
        )
    }

    fun prepararComida() = actualizar { actual ->
        actual.copy(
            energia = limitar(actual.energia + 10),
            actividad = limitar(actual.actividad + 8),
            hambre = limitar(actual.hambre - 5), // reducido de -12
            sed = limitar(actual.sed - 3)         // reducido de -6
        )
    }

    fun dormir() = actualizar { actual ->
        actual.copy(
            descanso = limitar(actual.descanso + 45), // subido de +30
            energia = limitar(actual.energia + 30),   // subido de +20
            hambre = limitar(actual.hambre - 8),
            sed = limitar(actual.sed - 6)
        )
    }

    fun siesta() = actualizar { actual ->
        actual.copy(
            descanso = limitar(actual.descanso + 25), // subido de +15
            energia = limitar(actual.energia + 15),   // subido de +10
            hambre = limitar(actual.hambre - 3),
            sed = limitar(actual.sed - 2)
        )
    }

    fun bano() = actualizar { actual ->
        actual.copy(
            energia = limitar(actual.energia + 15),
            hambre = limitar(actual.hambre - 3),
            limpieza = limitar(actual.limpieza + 20),
            sed = limitar(actual.sed - 3)
        )
    }

    fun ducharse() = actualizar { actual ->
        actual.copy(
            limpieza = limitar(actual.limpieza + 20),
            energia = limitar(actual.energia + 15),
            sed = limitar(actual.sed - 3)
        )
    }

    fun lavarDientes() = actualizar { actual ->
        actual.copy(limpieza = limitar(actual.limpieza + 12))
    }

    fun cuidarPiel() = actualizar { actual ->
        actual.copy(
            limpieza = limitar(actual.limpieza + 15),
            energia = limitar(actual.energia + 3)
        )
    }

    fun caminar() = actualizar { actual ->
        actual.copy(
            energia = limitar(actual.energia - 12),  // reducido de -25
            hambre = limitar(actual.hambre - 8),      // reducido de -15
            limpieza = limitar(actual.limpieza - 10), // reducido de -20
            sed = limitar(actual.sed - 10),            // reducido de -20
            actividad = limitar(actual.actividad + 25) // subido de +20
        )
    }

    fun estirar() = actualizar { actual ->
        actual.copy(
            energia = limitar(actual.energia - 5),   // reducido de -10
            hambre = limitar(actual.hambre - 5),      // reducido de -15
            sed = limitar(actual.sed - 5),             // reducido de -15
            actividad = limitar(actual.actividad + 15) // subido de +10
        )
    }

    fun tocarPiano() = actualizar { actual ->
        actual.copy(
            actividad = limitar(actual.actividad + 12),
            descanso = limitar(actual.descanso + 10),
            energia = limitar(actual.energia - 5)
        )
    }

    fun jugarPelota() = actualizar { actual ->
        actual.copy(
            actividad = limitar(actual.actividad + 15), // subido de +12
            descanso = limitar(actual.descanso - 8),    // reducido de -10
            energia = limitar(actual.energia - 10)       // reducido de -15
        )
    }

    fun pintar() = actualizar { actual ->
        actual.copy(
            actividad = limitar(actual.actividad + 10),
            descanso = limitar(actual.descanso + 10),
            energia = limitar(actual.energia + 5)
        )
    }

    fun resetearMedidores() = actualizar { actual ->
        actual.copy(
            energia = 50,
            hambre = 50,
            sed = 50,
            limpieza = 50,
            actividad = 50,
            descanso = 50
        )
    }

    fun acumularDia() = actualizar { actual ->
        actual.copy(
            sumaEnergia = actual.sumaEnergia + actual.energia,
            sumaHambre = actual.sumaHambre + actual.hambre,
            sumaSed = actual.sumaSed + actual.sed,
            sumaLimpieza = actual.sumaLimpieza + actual.limpieza,
            sumaActividad = actual.sumaActividad + actual.actividad,
            sumaDescanso = actual.sumaDescanso + actual.descanso,
            diasEvaluados = actual.diasEvaluados + 1
        )
    }

    fun avanzarSemana() = actualizar { actual ->
        actual.copy(
            semanaEmbarazo = (actual.semanaEmbarazo + 1).coerceAtMost(40)
        )
    }

    fun iniciarTiempo() {
        if (trabajoTiempo != null) return
        trabajoTiempo = viewModelScope.launch {
            while (true) {
                delay(10_000L)
                avanzarDia()
            }
        }
    }

    private fun avanzarDia() = actualizar { actual ->
        val nuevoDia = (actual.diaEmbarazo + 1).coerceAtMost(280)
        val nuevaSemana = ((nuevoDia - 1) / 7) + 1
        actual.copy(
            diaEmbarazo = nuevoDia,
            semanaEmbarazo = nuevaSemana.coerceAtMost(40),
            sumaEnergia = actual.sumaEnergia + actual.energia,
            sumaHambre = actual.sumaHambre + actual.hambre,
            sumaSed = actual.sumaSed + actual.sed,
            sumaLimpieza = actual.sumaLimpieza + actual.limpieza,
            sumaActividad = actual.sumaActividad + actual.actividad,
            sumaDescanso = actual.sumaDescanso + actual.descanso,
            diasEvaluados = actual.diasEvaluados + 1
        )
    }

    fun resetearJuego() {
        viewModelScope.launch {
            val petInicial = Pet(
                energia = 100,
                hambre = 100,
                sed = 100,
                limpieza = 100,
                actividad = 100,
                descanso = 100,
                semanaEmbarazo = 1,
                diaEmbarazo = 1,
                sumaEnergia = 0,
                sumaHambre = 0,
                sumaSed = 0,
                sumaLimpieza = 0,
                sumaActividad = 0,
                sumaDescanso = 0,
                diasEvaluados = 0
            )
            prefs.guardar(petInicial)
        }
    }

    private fun actualizar(bloque: (Pet) -> Pet) {
        viewModelScope.launch {
            prefs.guardar(bloque(pet.value))
        }
    }

    override fun onCleared() {
        trabajoTick?.cancel()
        trabajoTiempo?.cancel()
        super.onCleared()
    }
}