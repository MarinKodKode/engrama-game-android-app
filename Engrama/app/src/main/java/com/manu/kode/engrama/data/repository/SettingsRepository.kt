package com.manu.kode.engrama.data.repository

import com.manu.kode.engrama.domain.model.AppSettings
import com.manu.kode.engrama.domain.model.AppearanceMode
import com.manu.kode.engrama.domain.model.GameOperation
import com.manu.kode.engrama.domain.model.OperationSettings
import com.manu.kode.engrama.domain.model.TriviaCategory
import com.manu.kode.engrama.domain.model.TriviaDifficulty
import com.manu.kode.engrama.domain.model.WordCategory
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settings: Flow<AppSettings>

    suspend fun setHapticOnError(enabled: Boolean)
    suspend fun setHapticOnSound(enabled: Boolean)
    suspend fun setAppearance(mode: AppearanceMode)

    suspend fun setMathTimeLimit(seconds: Int)
    /** Solo ADD, SUBSTRACT, MULTIPLY o DIVIDE. */
    suspend fun setOperationSettings(operation: GameOperation, settings: OperationSettings)

    suspend fun setSpanishTimeLimit(seconds: Int)
    suspend fun setSpanishCategories(categories: List<WordCategory>)

    suspend fun setTriviaTimeLimit(seconds: Int)
    suspend fun setTriviaCategories(categories: List<TriviaCategory>)
    suspend fun setTriviaDifficulty(difficulty: TriviaDifficulty)

    suspend fun setProfileName(name: String)
    suspend fun setAvatarImageName(imageName: String)
    suspend fun setUsername(username: String?)
}
