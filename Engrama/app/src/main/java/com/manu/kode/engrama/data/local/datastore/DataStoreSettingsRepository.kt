package com.manu.kode.engrama.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.manu.kode.engrama.data.repository.SettingsRepository
import com.manu.kode.engrama.domain.model.AppSettings
import com.manu.kode.engrama.domain.model.AppearanceMode
import com.manu.kode.engrama.domain.model.GameOperation
import com.manu.kode.engrama.domain.model.OperationSettings
import com.manu.kode.engrama.domain.model.TriviaCategory
import com.manu.kode.engrama.domain.model.TriviaDifficulty
import com.manu.kode.engrama.domain.model.WordCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreSettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    override val settings: Flow<AppSettings> =
        dataStore.data.map(SettingsPreferencesMapper::toAppSettings)

    override suspend fun setHapticOnError(enabled: Boolean) =
        set(SettingsKeys.HAPTIC_ON_ERROR, enabled)

    override suspend fun setHapticOnSound(enabled: Boolean) =
        set(SettingsKeys.HAPTIC_ON_SOUND, enabled)

    override suspend fun setAppearance(mode: AppearanceMode) =
        set(SettingsKeys.APPEARANCE, mode.rawValue)

    override suspend fun setMathTimeLimit(seconds: Int) =
        set(SettingsKeys.MATH_TIME_LIMIT, seconds)

    override suspend fun setOperationSettings(operation: GameOperation, settings: OperationSettings) {
        require(operation.isMath) { "$operation no es una operación de Math" }
        val keys = SettingsKeys.operation(operation)
        dataStore.edit {
            it[keys.numberOfDigits] = settings.numberOfDigits
            it[keys.useNegativeNumbers] = settings.useNegativeNumbers
            it[keys.useDecimalNumbers] = settings.useDecimalNumbers
        }
    }

    override suspend fun setSpanishTimeLimit(seconds: Int) =
        set(SettingsKeys.SPANISH_TIME_LIMIT, seconds)

    override suspend fun setSpanishCategories(categories: List<WordCategory>) =
        set(SettingsKeys.SPANISH_ACTIVE_CATEGORIES, SettingsPreferencesMapper.toRawSet(categories))

    override suspend fun setTriviaTimeLimit(seconds: Int) =
        set(SettingsKeys.TRIVIA_TIME_LIMIT, seconds)

    override suspend fun setTriviaCategories(categories: List<TriviaCategory>) =
        set(SettingsKeys.TRIVIA_ACTIVE_CATEGORIES, SettingsPreferencesMapper.toRawSet(categories))

    override suspend fun setTriviaDifficulty(difficulty: TriviaDifficulty) =
        set(SettingsKeys.TRIVIA_DIFFICULTY, difficulty.rawValue)

    override suspend fun setProfileName(name: String) =
        set(SettingsKeys.PROFILE_NAME, name)

    override suspend fun setAvatarImageName(imageName: String) =
        set(SettingsKeys.PROFILE_AVATAR_IMAGE_NAME, imageName)

    override suspend fun setUsername(username: String?) {
        dataStore.edit {
            if (username == null) it.remove(SettingsKeys.PROFILE_USERNAME)
            else it[SettingsKeys.PROFILE_USERNAME] = username
        }
    }

    private suspend fun <T> set(key: Preferences.Key<T>, value: T) {
        dataStore.edit { it[key] = value }
    }
}
