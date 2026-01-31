package com.example.damagotchi_26.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.damagotchi_26.data.PetPrefs

class PetViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PetViewModel(PetPrefs(context.applicationContext)) as T
    }
}
