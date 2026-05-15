package com.example.damagotchi_26.data

import kotlinx.coroutines.flow.first
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
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
    private val KEY_SED = intPreferencesKey("sed")
    private val KEY_DESCANSO = intPreferencesKey("dormir")
    private val KEY_HIGIENE = intPreferencesKey("limpieza")
    private val KEY_SEMANA = intPreferencesKey("semana_embarazo")
    private val KEY_DIA = intPreferencesKey("dia_embarazo")

    private val KEY_SUMA_ENERGIA = intPreferencesKey("suma_energia")
    private val KEY_SUMA_HAMBRE = intPreferencesKey("suma_hambre")
    private val KEY_SUMA_SED = intPreferencesKey("suma_sed")
    private val KEY_SUMA_LIMPIEZA = intPreferencesKey("suma_limpieza")
    private val KEY_SUMA_ACTIVIDAD = intPreferencesKey("suma_actividad")
    private val KEY_SUMA_DESCANSO = intPreferencesKey("suma_descanso")
    private val KEY_DIAS_EVALUADOS = intPreferencesKey("dias_evaluados")

    private val AVISO_T1 = booleanPreferencesKey("aviso_trimestre_1")
    private val AVISO_T2 = booleanPreferencesKey("aviso_trimestre_14")
    private val AVISO_T3 = booleanPreferencesKey("aviso_trimestre_28")
    private val EVENTO_12 = booleanPreferencesKey("evento_semana_12")
    private val EVENTO_20 = booleanPreferencesKey("evento_semana_20")
    private val EVENTO_36 = booleanPreferencesKey("evento_semana_36")
    private val KEY_DIA_CONSEJO = intPreferencesKey("dia_consejo_mostrado")

    val petFlow: Flow<Pet> = context.dataStore.data.map { prefs ->
        Pet(
            hambre = prefs[KEY_HAMBRE] ?: 100,
            energia = prefs[KEY_ENERGIA] ?: 100,
            actividad = prefs[KEY_DIVERSION] ?: 100,
            sed = prefs[KEY_SED] ?: 100,
            descanso = prefs[KEY_DESCANSO] ?: 100,
            limpieza = prefs[KEY_HIGIENE] ?: 100,
            semanaEmbarazo = prefs[KEY_SEMANA] ?: 1,
            diaEmbarazo = prefs[KEY_DIA] ?: 1,
            sumaEnergia = prefs[KEY_SUMA_ENERGIA] ?: 0,
            sumaHambre = prefs[KEY_SUMA_HAMBRE] ?: 0,
            sumaSed = prefs[KEY_SUMA_SED] ?: 0,
            sumaLimpieza = prefs[KEY_SUMA_LIMPIEZA] ?: 0,
            sumaActividad = prefs[KEY_SUMA_ACTIVIDAD] ?: 0,
            sumaDescanso = prefs[KEY_SUMA_DESCANSO] ?: 0,
            diasEvaluados = prefs[KEY_DIAS_EVALUADOS] ?: 0
        )
    }

    suspend fun guardar(pet: Pet) {
        context.dataStore.edit { prefs ->
            prefs[KEY_HAMBRE] = pet.hambre
            prefs[KEY_ENERGIA] = pet.energia
            prefs[KEY_DIVERSION] = pet.actividad
            prefs[KEY_SED] = pet.sed
            prefs[KEY_DESCANSO] = pet.descanso
            prefs[KEY_HIGIENE] = pet.limpieza
            prefs[KEY_SEMANA] = pet.semanaEmbarazo
            prefs[KEY_DIA] = pet.diaEmbarazo
            prefs[KEY_SUMA_ENERGIA] = pet.sumaEnergia
            prefs[KEY_SUMA_HAMBRE] = pet.sumaHambre
            prefs[KEY_SUMA_SED] = pet.sumaSed
            prefs[KEY_SUMA_LIMPIEZA] = pet.sumaLimpieza
            prefs[KEY_SUMA_ACTIVIDAD] = pet.sumaActividad
            prefs[KEY_SUMA_DESCANSO] = pet.sumaDescanso
            prefs[KEY_DIAS_EVALUADOS] = pet.diasEvaluados
        }
    }

    suspend fun yaMostradoAviso(umbral: Int): Boolean {
        val key = when (umbral) {
            1 -> AVISO_T1
            14 -> AVISO_T2
            28 -> AVISO_T3
            else -> return true
        }
        return context.dataStore.data.first()[key] ?: false
    }

    suspend fun marcarAvisoMostrado(umbral: Int) {
        val key = when (umbral) {
            1 -> AVISO_T1
            14 -> AVISO_T2
            28 -> AVISO_T3
            else -> return
        }
        context.dataStore.edit { prefs ->
            prefs[key] = true
        }
    }

    suspend fun yaMostradoEvento(semana: Int): Boolean {
        val key = when (semana) {
            12 -> EVENTO_12
            20 -> EVENTO_20
            36 -> EVENTO_36
            else -> return true
        }
        return context.dataStore.data.first()[key] ?: false
    }

    suspend fun marcarEventoMostrado(semana: Int) {
        val key = when (semana) {
            12 -> EVENTO_12
            20 -> EVENTO_20
            36 -> EVENTO_36
            else -> return
        }
        context.dataStore.edit { prefs ->
            prefs[key] = true
        }
    }

    suspend fun ultimoDiaConsejoMostrado(): Int {
        return context.dataStore.data.first()[KEY_DIA_CONSEJO] ?: 0
    }

    suspend fun marcarConsejoDia(dia: Int) {
        context.dataStore.edit { prefs ->
            prefs[KEY_DIA_CONSEJO] = dia
        }
    }

    // Resetea todos los avisos y eventos para volver a empezar
    suspend fun resetearAvisos() {
        context.dataStore.edit { prefs ->
            prefs[AVISO_T1] = false
            prefs[AVISO_T2] = false
            prefs[AVISO_T3] = false
            prefs[EVENTO_12] = false
            prefs[EVENTO_20] = false
            prefs[EVENTO_36] = false
            prefs[KEY_DIA_CONSEJO] = 0
        }
    }
}