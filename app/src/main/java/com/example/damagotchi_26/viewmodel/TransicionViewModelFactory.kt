package com.example.damagotchi_26.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.damagotchi_26.data.PetPrefs

class TransicionViewModelFactory(
    private val petPrefs: PetPrefs // <-- Cambia Context por PetPrefs
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransicionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransicionViewModel(petPrefs) as T // Ya no necesitas crear PetPrefs aquí
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}