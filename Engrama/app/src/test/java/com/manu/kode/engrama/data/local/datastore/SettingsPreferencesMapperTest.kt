package com.manu.kode.engrama.data.local.datastore

import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.mutablePreferencesOf
import com.manu.kode.engrama.domain.model.AppSettings
import com.manu.kode.engrama.domain.model.AppearanceMode
import com.manu.kode.engrama.domain.model.OperationSettings
import com.manu.kode.engrama.domain.model.TriviaCategory
import com.manu.kode.engrama.domain.model.TriviaDifficulty
import com.manu.kode.engrama.domain.model.WordCategory
import com.manu.kode.engrama.testutil.LogSinkRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SettingsPreferencesMapperTest {

    @get:Rule
    val log = LogSinkRule()

    @Test
    fun keyNames_matchIosPaths() {
        assertEquals(
            listOf(
                "math.timeLimit",
                "math.add.numberOfDigits", "math.add.useNegativeNumbers", "math.add.useDecimalNumbers",
                "math.substract.numberOfDigits", "math.substract.useNegativeNumbers", "math.substract.useDecimalNumbers",
                "math.multiply.numberOfDigits", "math.multiply.useNegativeNumbers", "math.multiply.useDecimalNumbers",
                "math.divide.numberOfDigits", "math.divide.useNegativeNumbers", "math.divide.useDecimalNumbers",
                "spanish.timeLimit", "spanish.activeCategories",
                "trivia.timeLimit", "trivia.activeCategories", "trivia.difficulty",
                "profile.name", "profile.uid", "profile.avatarImageName", "profile.username",
                "hapticOnError", "hapticOnSound", "appearance"
            ),
            SettingsKeys.all.map { it.name }
        )
    }

    @Test
    fun emptyPreferences_giveIosDefaults_withoutLogs() {
        val settings = SettingsPreferencesMapper.toAppSettings(emptyPreferences())

        assertEquals(AppSettings(), settings)
        assertEquals(30, settings.math.timeLimit)
        assertEquals(OperationSettings(1, false, false), settings.math.substract)
        assertEquals(30, settings.spanish.timeLimit)
        assertEquals(
            listOf(WordCategory.VERBO, WordCategory.SUSTANTIVO, WordCategory.ADJETIVO, WordCategory.ADVERBIO),
            settings.spanish.activeCategories
        )
        assertEquals(60, settings.trivia.timeLimit)
        assertEquals(TriviaCategory.entries, settings.trivia.activeCategories)
        assertEquals(TriviaDifficulty.MEDIUM, settings.trivia.difficulty)
        assertEquals("Jugador", settings.profile.name)
        assertEquals("avatar_blue", settings.profile.avatarImageName)
        assertNull(settings.profile.username)
        assertTrue(settings.hapticOnError)
        assertTrue(settings.hapticOnSound)
        assertEquals(AppearanceMode.SYSTEM, settings.appearance)
        assertTrue(log.entries.isEmpty())
    }

    @Test
    fun everyKey_isReadIntoItsField() {
        val prefs = mutablePreferencesOf(
            SettingsKeys.MATH_TIME_LIMIT to 75,
            SettingsKeys.MATH_ADD.numberOfDigits to 2,
            SettingsKeys.MATH_SUBSTRACT.numberOfDigits to 3,
            SettingsKeys.MATH_SUBSTRACT.useNegativeNumbers to true,
            SettingsKeys.MATH_MULTIPLY.useDecimalNumbers to true,
            SettingsKeys.MATH_DIVIDE.numberOfDigits to 2,
            SettingsKeys.SPANISH_TIME_LIMIT to 45,
            SettingsKeys.SPANISH_ACTIVE_CATEGORIES to setOf("adverbio", "verbo", "conjuncion"),
            SettingsKeys.TRIVIA_TIME_LIMIT to 90,
            SettingsKeys.TRIVIA_ACTIVE_CATEGORIES to setOf("historia", "español"),
            SettingsKeys.TRIVIA_DIFFICULTY to "hard",
            SettingsKeys.PROFILE_NAME to "Manu",
            SettingsKeys.PROFILE_UID to "uid-123",
            SettingsKeys.PROFILE_AVATAR_IMAGE_NAME to "avatar_gold",
            SettingsKeys.PROFILE_USERNAME to "manu",
            SettingsKeys.HAPTIC_ON_ERROR to false,
            SettingsKeys.HAPTIC_ON_SOUND to false,
            SettingsKeys.APPEARANCE to "dark"
        )

        val s = SettingsPreferencesMapper.toAppSettings(prefs)

        assertEquals(75, s.math.timeLimit)
        assertEquals(OperationSettings(2, false, false), s.math.add)
        assertEquals(OperationSettings(3, true, false), s.math.substract)
        assertEquals(OperationSettings(1, false, true), s.math.multiply)
        assertEquals(OperationSettings(2, false, false), s.math.divide)
        assertEquals(45, s.spanish.timeLimit)
        // Orden canónico del enum, no el del set.
        assertEquals(
            listOf(WordCategory.VERBO, WordCategory.ADVERBIO, WordCategory.CONJUNCION),
            s.spanish.activeCategories
        )
        assertEquals(90, s.trivia.timeLimit)
        assertEquals(listOf(TriviaCategory.ESPANOL, TriviaCategory.HISTORIA), s.trivia.activeCategories)
        assertEquals(TriviaDifficulty.HARD, s.trivia.difficulty)
        assertEquals("Manu", s.profile.name)
        assertEquals("uid-123", s.profile.uid)
        assertEquals("avatar_gold", s.profile.avatarImageName)
        assertEquals("manu", s.profile.username)
        assertEquals(false, s.hapticOnError)
        assertEquals(false, s.hapticOnSound)
        assertEquals(AppearanceMode.DARK, s.appearance)
        assertTrue(log.entries.isEmpty())
    }

    @Test
    fun invalidEnumRaw_fallsBackToDefault_withWarning() {
        val prefs = mutablePreferencesOf(
            SettingsKeys.TRIVIA_DIFFICULTY to "extreme",
            SettingsKeys.APPEARANCE to "sepia"
        )

        val s = SettingsPreferencesMapper.toAppSettings(prefs)

        assertEquals(TriviaDifficulty.MEDIUM, s.trivia.difficulty)
        assertEquals(AppearanceMode.SYSTEM, s.appearance)
        assertTrue(log.warnings.any { it.message.contains("trivia.difficulty") })
        assertTrue(log.warnings.any { it.message.contains("appearance") })
    }

    @Test
    fun emptyCategorySet_fallsBackToDefault_withWarning() {
        val prefs = mutablePreferencesOf(SettingsKeys.SPANISH_ACTIVE_CATEGORIES to emptySet())

        val s = SettingsPreferencesMapper.toAppSettings(prefs)

        assertEquals(AppSettings().spanish.activeCategories, s.spanish.activeCategories)
        assertTrue(log.warnings.any { it.message.contains("spanish.activeCategories") })
    }

    @Test
    fun categorySetWithInvalidValue_fallsBackToDefault_withWarning() {
        val prefs = mutablePreferencesOf(
            SettingsKeys.TRIVIA_ACTIVE_CATEGORIES to setOf("historia", "astrologia")
        )

        val s = SettingsPreferencesMapper.toAppSettings(prefs)

        assertEquals(TriviaCategory.entries, s.trivia.activeCategories)
        assertTrue(log.warnings.any { it.message.contains("trivia.activeCategories") })
    }

    @Test
    fun toRawSet_usesRawValues() {
        assertEquals(
            setOf("español", "quimica"),
            SettingsPreferencesMapper.toRawSet(listOf(TriviaCategory.QUIMICA, TriviaCategory.ESPANOL))
        )
    }
}
