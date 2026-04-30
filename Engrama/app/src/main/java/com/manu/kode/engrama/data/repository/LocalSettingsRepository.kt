package com.manu.kode.engrama.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.manu.kode.engrama.data.model.AppSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore("app_settings")

@Singleton
class LocalSettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) : SettingsRepository {

    private val key = stringPreferencesKey("settings_json")

    override val settings: Flow<AppSettings> = context.settingsDataStore.data
        .map { prefs ->
            val json = prefs[key]
            if (json != null) {
                try {
                    gson.fromJson(json, AppSettings::class.java)
                } catch (e: Exception) {
                    AppSettings()
                }
            } else {
                AppSettings()
            }
        }

    override suspend fun save(settings: AppSettings) {
        context.settingsDataStore.edit { prefs ->
            prefs[key] = gson.toJson(settings)
        }
    }
}