package com.example.damagotchi_26.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.damagotchi_26.domain.MomentoDia
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.launch

class TransicionViewModel : ViewModel() {

    var _momentoDia = MutableStateFlow(MomentoDia.DIA)
        private set
    val momentoDia: StateFlow<MomentoDia> = _momentoDia


    private var trabajoLuz: Job? = null

    fun iniciarCicloLuz() {
        if (trabajoLuz != null) return

        trabajoLuz = viewModelScope.launch {
            while (true) {
                delay((1.24 * 60 * 60 * 1000).toLong()) // 1,24 horas
                _momentoDia.value =
                    if (_momentoDia.value == MomentoDia.DIA) MomentoDia.NOCHE
                else MomentoDia.DIA
            }
        }
    }


    override fun onCleared() {
        trabajoLuz?.cancel()
        super.onCleared()
    }
}
