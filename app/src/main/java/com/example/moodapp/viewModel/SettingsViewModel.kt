package com.example.moodapp.viewModel

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Locale

data class AppSettings(
    val notificationsEnabled: Boolean = true,
    val soundsEnabled: Boolean = true,
    val appLanguage: String = "English"
)

class SettingsViewModel(private val dataStore: DataStore<Preferences>) : ViewModel() {
    companion object {
        private val NOTIFICATIONS_KEY = booleanPreferencesKey("notifications_enabled")
        private val SOUNDS_KEY = booleanPreferencesKey("sounds_enabled")
        private val LANGUAGE_KEY = stringPreferencesKey("app_language")
    }

    private val _uiState = MutableStateFlow(AppSettings())
    val uiState: StateFlow<AppSettings> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            dataStore.data.map { preferences ->
                AppSettings(
                    notificationsEnabled = preferences[NOTIFICATIONS_KEY] ?: true,
                    soundsEnabled = preferences[SOUNDS_KEY] ?: true,
                    appLanguage = preferences[LANGUAGE_KEY] ?: "English"
                )
            }.collect { settings ->
                _uiState.value = settings
            }
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[NOTIFICATIONS_KEY] = enabled
            }
        }
    }

    fun toggleSounds(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[SOUNDS_KEY] = enabled
            }
        }
    }

    //for 2 langs!!!!
    fun setAppLanguage(context: Context, languageCode: String) {
        viewModelScope.launch {
            // Зберегти вибір мови
            dataStore.edit { preferences ->
                preferences[LANGUAGE_KEY] = languageCode
            }

            // Оновити локаль додатка
            updateLocale(context, languageCode)
        }
    }

    fun initializeLanguage(context: Context) {
        viewModelScope.launch {
            dataStore.data.collect { preferences ->
                preferences[LANGUAGE_KEY]?.let { lang ->
                    if (lang.isNotEmpty()) {
                        updateLocale(context, lang)
                    }
                }
            }
        }
    }

    private fun updateLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources = context.resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}