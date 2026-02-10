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


/*
* Hace que los medidores suban o bajen su porcentaje
* Implementacion de la temporalizacion del embarazo.

*/


class PetViewModel( private val prefs: PetPrefs
) : ViewModel() {

    val pet: StateFlow<Pet> = prefs.petFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(3_000),
        initialValue = Pet()
    )

    private var trabajoTick: Job? = null
    private var trabajoTiempo: Job? = null

    private val minutosRealesPorDiaFicticio = 155L   // modo REAL
 // private val minutosRealesPorDiaFicticio = 1L  // modo DEMO (prueba rápida)



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
            hambre = limitar(actual.hambre + 15),
            energia = limitar(actual.energia + 15),
            limpieza = limitar(actual.limpieza - 2),
            actividad = limitar(actual.actividad - 1),
            descanso = limitar(actual.descanso - 1)
        )
    }

    fun hidratar() = actualizar { actual ->
        actual.copy(
            sed = limitar(actual.sed + 15),
            hambre = limitar(actual.hambre + 5),
            energia = limitar(actual.energia + 10)
        )
    }

    fun verTV() = actualizar { actual ->
        actual.copy(
            sed = limitar(actual.sed - 1),
            hambre = limitar(actual.hambre - 2),
            descanso = limitar(actual.descanso + 5),
            actividad = limitar(actual.actividad + 2),
            energia = limitar(actual.energia + 5)
        )
    }

    fun leer() = actualizar { actual ->
        actual.copy(
            sed = limitar(actual.sed - 1),
            hambre = limitar(actual.hambre - 2),
            descanso = limitar(actual.descanso + 5),
            actividad = limitar(actual.actividad + 5),
            energia = limitar(actual.energia + 5)
        )
    }


    fun dormir() = actualizar { actual ->
        actual.copy(
            descanso = limitar(actual.descanso + 20),
            energia = limitar(actual.energia + 10),
            hambre = limitar(actual.hambre - 3),
            sed = limitar(actual.sed - 2)
        )
    }

    fun higiene() = actualizar { actual ->
        actual.copy(
            energia = limitar(actual.energia + 10),
            hambre = limitar(actual.hambre - 3),
            limpieza = limitar(actual.limpieza + 15),
            sed = limitar(actual.sed - 2)
        )
    }

    fun caminar() = actualizar { actual ->
        actual.copy(
            energia = limitar(actual.energia + 8),
            hambre = limitar(actual.hambre - 10),
            limpieza = limitar(actual.limpieza - 10),
            sed = limitar(actual.sed - 10),
            actividad = limitar(actual.actividad + 15)
        )
    }

    fun yoga() = actualizar { actual ->
        actual.copy(
            energia = limitar(actual.energia + 8),
            hambre = limitar(actual.hambre - 5),
            limpieza = limitar(actual.limpieza + 8),
            sed = limitar(actual.sed - 2),
            actividad = limitar(actual.actividad + 15)

        )
    }

    fun avanzarSemana() = actualizar { actual ->
        actual.copy(
            semanaEmbarazo = (actual.semanaEmbarazo + 1).coerceAtMost(40) //avanza una semana hasta llegar a 40
        )
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


    fun iniciarTiempo() {
        if (trabajoTiempo != null) return

        trabajoTiempo = viewModelScope.launch {
            while (true) {
                delay(155 * 60 * 1000L) // 155 min reales → 1 día ficticio ()
                avanzarDia()
            }
        }
    }

    private fun avanzarDia() {
        val actual = pet.value
        val nuevoDia = (actual.diaEmbarazo + 1).coerceAtMost(280)
        val nuevaSemana = ((nuevoDia - 1) / 7) + 1

        val actualizado = actual.copy(
            diaEmbarazo = nuevoDia,
            semanaEmbarazo = nuevaSemana.coerceAtMost(40)
        )

        //Coroutine (es como "tranquilo realiza esto sin prisas en suguendo plano.., Sin prisa pero sin pausa"(hilos))
        viewModelScope.launch {
            prefs.guardar(pet = actualizado)
        }
    }



}

