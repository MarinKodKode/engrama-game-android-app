package com.manu.kode.engrama.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.manu.kode.engrama.data.model.GameSession
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore("game_sessions")

@Singleton
class LocalSessionRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) : SessionRepository {

    private val key = stringPreferencesKey("sessions_json")

    override val sessions: Flow<List<GameSession>> = context.sessionDataStore.data
        .map { prefs ->
            val json = prefs[key]
            if (json != null) {
                try {
                    val type = object : TypeToken<List<GameSession>>() {}.type
                    gson.fromJson(json, type) ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }
        }

    override suspend fun addSession(session: GameSession) {
        context.sessionDataStore.edit { prefs ->
            val current = try {
                val json = prefs[key]
                if (json != null) {
                    val type = object : TypeToken<List<GameSession>>() {}.type
                    gson.fromJson<List<GameSession>>(json, type) ?: emptyList()
                } else emptyList()
            } catch (e: Exception) {
                emptyList()
            }
            val updated = listOf(session) + current
            prefs[key] = gson.toJson(updated)
        }
    }

    override suspend fun clearAll() {
        context.sessionDataStore.edit { it.clear() }
    }
}