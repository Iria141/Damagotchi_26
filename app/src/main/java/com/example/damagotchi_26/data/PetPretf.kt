package com.example.damagotchi_26.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.damagotchi_26.domain.Pet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "pet_prefs")

class PetPrefs(private val context: Context) {

    private val KEY_HAMBRE = intPreferencesKey("hambre")
    private val KEY_ENERGIA = intPreferencesKey("energia")
    private val KEY_DIVERSION = intPreferencesKey("diversion")

    val petFlow: Flow<Pet> = context.dataStore.data.map { prefs ->
        Pet(
            hambre = prefs[KEY_HAMBRE] ?: 100,
            energia = prefs[KEY_ENERGIA] ?: 100,
            actividad = prefs[KEY_DIVERSION] ?: 100
        )
    }

    suspend fun guardar(pet: Pet) {
        context.dataStore.edit { prefs ->
            prefs[KEY_HAMBRE] = pet.hambre
            prefs[KEY_ENERGIA] = pet.energia
            prefs[KEY_DIVERSION] = pet.actividad
        }
    }
}
