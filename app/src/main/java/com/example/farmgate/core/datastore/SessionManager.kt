package com.example.farmgate.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore by preferencesDataStore(name = "farmgate_session")

class SessionManager(
    private val context: Context
) {

    private object PreferencesKeys {
        val TOKEN = stringPreferencesKey("token")
        val ROLE = stringPreferencesKey("role")
        val USER_ID = stringPreferencesKey("user_id")
        val SELECTED_CITY_ID = stringPreferencesKey("selected_city_id")
        val SELECTED_CITY_NAME = stringPreferencesKey("selected_city_name")
    }

    val sessionFlow: Flow<SessionState> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences.toSessionState()
        }

    suspend fun saveSession(
        token: String,
        role: String,
        userId: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.TOKEN] = token
            preferences[PreferencesKeys.ROLE] = role
            preferences[PreferencesKeys.USER_ID] = userId
        }
    }

    suspend fun saveSelectedCity(
        cityId: String,
        cityName: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_CITY_ID] = cityId
            preferences[PreferencesKeys.SELECTED_CITY_NAME] = cityName
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun clearSelectedCity() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.SELECTED_CITY_ID)
            preferences.remove(PreferencesKeys.SELECTED_CITY_NAME)
        }
    }

    private fun Preferences.toSessionState(): SessionState {
        return SessionState(
            token = this[PreferencesKeys.TOKEN].orEmpty(),
            role = this[PreferencesKeys.ROLE].orEmpty(),
            userId = this[PreferencesKeys.USER_ID].orEmpty(),
            selectedCityId = this[PreferencesKeys.SELECTED_CITY_ID].orEmpty(),
            selectedCityName = this[PreferencesKeys.SELECTED_CITY_NAME].orEmpty()
        )
    }
}