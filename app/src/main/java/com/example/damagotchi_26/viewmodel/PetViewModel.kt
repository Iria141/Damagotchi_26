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


//Hace que los medidores suban o bajen su porcentaje
class PetViewModel(
    private val prefs: PetPrefs
) : ViewModel() {

    val pet: StateFlow<Pet> = prefs.petFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = Pet()
    )

    private var trabajoTick: Job? = null

    fun iniciarTick() {
        if (trabajoTick != null) return

        trabajoTick = viewModelScope.launch {
            while (true) {
                delay(3000)
                val actual = pet.value
                val actualizado = actual.copy(
                    hambre = limitar(actual.hambre - 2),
                    energia = limitar(actual.energia - 1),
                    actividad = limitar(actual.actividad - 2),
                    sed = limitar(actual.sed - 2),
                    limpieza = limitar (actual.limpieza -1),
                    descanso = limitar(actual.descanso - 2)
                )
                prefs.guardar(actualizado)
            }
        }
    }

    fun alimentar() = actualizar { actual ->
        actual.copy(
            hambre = limitar(actual.hambre + 15),
            limpieza = limitar(actual.limpieza - 5),
            actividad = limitar(actual.actividad - 2),
            descanso = limitar(actual.descanso - 5)
        )
    }

    fun hidratar() = actualizar { actual ->
        actual.copy(
            sed = limitar(actual.sed + 15),
            hambre = limitar(actual.hambre + 5)
        )
    }

    fun verTV() = actualizar { actual ->
        actual.copy(
            sed = limitar(actual.sed - 10),
            hambre = limitar(actual.hambre - 5),
            descanso = limitar(actual.descanso + 5),
            actividad = limitar(actual.actividad -2)
        )
    }

    fun leer() = actualizar { actual ->
        actual.copy(
            sed = limitar(actual.sed -10),
            hambre = limitar(actual.hambre - 5),
            descanso = limitar(actual.descanso + 5),
            actividad = limitar(actual.actividad -2)
        )
    }


    fun dormir() = actualizar { actual ->
        actual.copy(
            energia = limitar(actual.energia + 20),
            hambre = limitar(actual.hambre - 5)
        )
    }

    fun jugar() = actualizar { actual ->
        actual.copy(
            actividad = limitar(actual.actividad + 18),
            energia = limitar(actual.energia - 6),
            hambre = limitar(actual.hambre - 4)
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
}
