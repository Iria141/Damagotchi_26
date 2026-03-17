package com.example.damagotchi_26.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        private val REMEMBER_ME = booleanPreferencesKey("remember_me")
        private val WELCOME_SEEN = booleanPreferencesKey("welcome_seen")
    }

    val rememberMeFlow: Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[REMEMBER_ME] ?: false }

    val welcomeSeenFlow: Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[WELCOME_SEEN] ?: false }

    suspend fun setRememberMe(value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[REMEMBER_ME] = value
        }
    }

    suspend fun setWelcomeSeen(value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[WELCOME_SEEN] = value
        }
    }
}