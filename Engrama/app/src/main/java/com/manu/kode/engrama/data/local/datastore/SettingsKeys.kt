package com.manu.kode.engrama.data.local.datastore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.manu.kode.engrama.domain.model.GameOperation

/** Una clave por campo, nombrada con la ruta del JSON Codable de iOS (AppSettings y anidados). */
object SettingsKeys {

    class OperationKeys(prefix: String) {
        val numberOfDigits = intPreferencesKey("$prefix.numberOfDigits")
        val useNegativeNumbers = booleanPreferencesKey("$prefix.useNegativeNumbers")
        val useDecimalNumbers = booleanPreferencesKey("$prefix.useDecimalNumbers")
    }

    val MATH_TIME_LIMIT = intPreferencesKey("math.timeLimit")
    val MATH_ADD = OperationKeys("math.add")
    val MATH_SUBSTRACT = OperationKeys("math.substract")
    val MATH_MULTIPLY = OperationKeys("math.multiply")
    val MATH_DIVIDE = OperationKeys("math.divide")

    val SPANISH_TIME_LIMIT = intPreferencesKey("spanish.timeLimit")
    val SPANISH_ACTIVE_CATEGORIES = stringSetPreferencesKey("spanish.activeCategories")

    val TRIVIA_TIME_LIMIT = intPreferencesKey("trivia.timeLimit")
    val TRIVIA_ACTIVE_CATEGORIES = stringSetPreferencesKey("trivia.activeCategories")
    val TRIVIA_DIFFICULTY = stringPreferencesKey("trivia.difficulty")

    val PROFILE_NAME = stringPreferencesKey("profile.name")
    val PROFILE_UID = stringPreferencesKey("profile.uid")
    val PROFILE_AVATAR_IMAGE_NAME = stringPreferencesKey("profile.avatarImageName")
    val PROFILE_USERNAME = stringPreferencesKey("profile.username")

    val HAPTIC_ON_ERROR = booleanPreferencesKey("hapticOnError")
    val HAPTIC_ON_SOUND = booleanPreferencesKey("hapticOnSound")
    val APPEARANCE = stringPreferencesKey("appearance")

    fun operation(operation: GameOperation): OperationKeys = when (operation) {
        GameOperation.ADD -> MATH_ADD
        GameOperation.SUBSTRACT -> MATH_SUBSTRACT
        GameOperation.MULTIPLY -> MATH_MULTIPLY
        GameOperation.DIVIDE -> MATH_DIVIDE
        else -> throw IllegalArgumentException("$operation no es una operación de Math")
    }

    /** Todas las claves, para comprobar nombres en tests. */
    val all: List<Preferences.Key<*>>
        get() = listOf(
            MATH_TIME_LIMIT,
            *listOf(MATH_ADD, MATH_SUBSTRACT, MATH_MULTIPLY, MATH_DIVIDE)
                .flatMap { listOf(it.numberOfDigits, it.useNegativeNumbers, it.useDecimalNumbers) }
                .toTypedArray(),
            SPANISH_TIME_LIMIT, SPANISH_ACTIVE_CATEGORIES,
            TRIVIA_TIME_LIMIT, TRIVIA_ACTIVE_CATEGORIES, TRIVIA_DIFFICULTY,
            PROFILE_NAME, PROFILE_UID, PROFILE_AVATAR_IMAGE_NAME, PROFILE_USERNAME,
            HAPTIC_ON_ERROR, HAPTIC_ON_SOUND, APPEARANCE
        )
}
