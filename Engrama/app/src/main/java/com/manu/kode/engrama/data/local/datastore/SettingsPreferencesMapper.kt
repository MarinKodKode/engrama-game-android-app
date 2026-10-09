package com.manu.kode.engrama.data.local.datastore

import androidx.datastore.preferences.core.Preferences
import com.manu.kode.engrama.core.logging.AppLog
import com.manu.kode.engrama.domain.model.AppSettings
import com.manu.kode.engrama.domain.model.AppearanceMode
import com.manu.kode.engrama.domain.model.MathSettings
import com.manu.kode.engrama.domain.model.OperationSettings
import com.manu.kode.engrama.domain.model.RawValueEnum
import com.manu.kode.engrama.domain.model.SpanishSettings
import com.manu.kode.engrama.domain.model.TriviaCategory
import com.manu.kode.engrama.domain.model.TriviaDifficulty
import com.manu.kode.engrama.domain.model.TriviaSettings
import com.manu.kode.engrama.domain.model.UserProfile
import com.manu.kode.engrama.domain.model.WordCategory
import com.manu.kode.engrama.domain.model.fromRaw

/**
 * Preferences → AppSettings. Clave ausente → default iOS sin log.
 * Valor inválido → default iOS con Log.w.
 */
object SettingsPreferencesMapper {

    private const val TAG = "SettingsMapper"

    fun toAppSettings(prefs: Preferences): AppSettings {
        val defaults = AppSettings()
        return AppSettings(
            math = MathSettings(
                timeLimit = prefs[SettingsKeys.MATH_TIME_LIMIT] ?: defaults.math.timeLimit,
                add = prefs.operation(SettingsKeys.MATH_ADD),
                substract = prefs.operation(SettingsKeys.MATH_SUBSTRACT),
                multiply = prefs.operation(SettingsKeys.MATH_MULTIPLY),
                divide = prefs.operation(SettingsKeys.MATH_DIVIDE)
            ),
            spanish = SpanishSettings(
                timeLimit = prefs[SettingsKeys.SPANISH_TIME_LIMIT] ?: defaults.spanish.timeLimit,
                activeCategories = categories(
                    prefs[SettingsKeys.SPANISH_ACTIVE_CATEGORIES],
                    SettingsKeys.SPANISH_ACTIVE_CATEGORIES.name,
                    defaults.spanish.activeCategories
                )
            ),
            trivia = TriviaSettings(
                timeLimit = prefs[SettingsKeys.TRIVIA_TIME_LIMIT] ?: defaults.trivia.timeLimit,
                activeCategories = categories<TriviaCategory>(
                    prefs[SettingsKeys.TRIVIA_ACTIVE_CATEGORIES],
                    SettingsKeys.TRIVIA_ACTIVE_CATEGORIES.name,
                    defaults.trivia.activeCategories
                ),
                difficulty = enumOrDefault(
                    prefs[SettingsKeys.TRIVIA_DIFFICULTY],
                    SettingsKeys.TRIVIA_DIFFICULTY.name,
                    TriviaDifficulty.MEDIUM
                )
            ),
            profile = UserProfile(
                name = prefs[SettingsKeys.PROFILE_NAME] ?: defaults.profile.name,
                uid = prefs[SettingsKeys.PROFILE_UID] ?: defaults.profile.uid,
                avatarImageName = prefs[SettingsKeys.PROFILE_AVATAR_IMAGE_NAME]
                    ?: defaults.profile.avatarImageName,
                username = prefs[SettingsKeys.PROFILE_USERNAME]
            ),
            hapticOnError = prefs[SettingsKeys.HAPTIC_ON_ERROR] ?: defaults.hapticOnError,
            hapticOnSound = prefs[SettingsKeys.HAPTIC_ON_SOUND] ?: defaults.hapticOnSound,
            appearance = enumOrDefault(
                prefs[SettingsKeys.APPEARANCE],
                SettingsKeys.APPEARANCE.name,
                AppearanceMode.SYSTEM
            )
        )
    }

    private fun Preferences.operation(keys: SettingsKeys.OperationKeys): OperationSettings {
        val defaults = OperationSettings()
        return OperationSettings(
            numberOfDigits = this[keys.numberOfDigits] ?: defaults.numberOfDigits,
            useNegativeNumbers = this[keys.useNegativeNumbers] ?: defaults.useNegativeNumbers,
            useDecimalNumbers = this[keys.useDecimalNumbers] ?: defaults.useDecimalNumbers
        )
    }

    private inline fun <reified T> enumOrDefault(raw: String?, key: String, default: T): T
            where T : Enum<T>, T : RawValueEnum {
        if (raw == null) return default
        return fromRaw<T>(raw) ?: default.also {
            AppLog.w(TAG, "$key inválido (\"$raw\"); se usa el default \"${default.rawValue}\"")
        }
    }

    /** Set persistido → lista en el orden canónico del enum. */
    private inline fun <reified T> categories(raw: Set<String>?, key: String, default: List<T>): List<T>
            where T : Enum<T>, T : RawValueEnum {
        if (raw == null) return default
        val parsed = raw.map { fromRaw<T>(it) }
        if (raw.isEmpty() || parsed.any { it == null }) {
            AppLog.w(TAG, "$key inválido ($raw); se usa el default")
            return default
        }
        return enumValues<T>().filter { it in parsed }
    }

    /** Lista → valor a persistir. */
    fun <T : RawValueEnum> toRawSet(values: List<T>): Set<String> =
        values.map { it.rawValue }.toSet()
}
