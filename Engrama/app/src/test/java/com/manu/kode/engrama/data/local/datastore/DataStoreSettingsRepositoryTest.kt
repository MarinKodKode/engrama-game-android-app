package com.manu.kode.engrama.data.local.datastore

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.manu.kode.engrama.domain.model.AppearanceMode
import com.manu.kode.engrama.domain.model.GameOperation
import com.manu.kode.engrama.domain.model.OperationSettings
import com.manu.kode.engrama.domain.model.TriviaCategory
import com.manu.kode.engrama.domain.model.TriviaDifficulty
import com.manu.kode.engrama.domain.model.WordCategory
import com.manu.kode.engrama.testutil.LogSinkRule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

/** DataStore real sobre un archivo temporal: updates por campo y uid estable. */
class DataStoreSettingsRepositoryTest {

    @get:Rule
    val tmp = TemporaryFolder()

    @get:Rule
    val log = LogSinkRule()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @After
    fun tearDown() = scope.cancel()

    private fun dataStore(file: File) = PreferenceDataStoreFactory.create(
        scope = scope,
        migrations = listOf(ProfileUidMigration()),
        produceFile = { file }
    )

    @Test
    fun uid_isGeneratedOnce_andStable() = runBlocking {
        val repo = DataStoreSettingsRepository(dataStore(File(tmp.root, "a.preferences_pb")))

        val first = repo.settings.first().profile.uid
        repo.setProfileName("Otro")
        val second = repo.settings.first().profile.uid

        assertTrue(first.isNotEmpty())
        assertEquals(first, second)
    }

    @Test
    fun updates_writeEachFieldUnderIosKey() = runBlocking {
        val store = dataStore(File(tmp.root, "b.preferences_pb"))
        val repo = DataStoreSettingsRepository(store)

        repo.setHapticOnError(false)
        repo.setHapticOnSound(false)
        repo.setAppearance(AppearanceMode.LIGHT)
        repo.setMathTimeLimit(45)
        repo.setOperationSettings(GameOperation.SUBSTRACT, OperationSettings(3, true, false))
        repo.setSpanishTimeLimit(60)
        repo.setSpanishCategories(listOf(WordCategory.ARTICULO, WordCategory.VERBO))
        repo.setTriviaTimeLimit(75)
        repo.setTriviaCategories(listOf(TriviaCategory.FISICA))
        repo.setTriviaDifficulty(TriviaDifficulty.EASY)
        repo.setProfileName("Manu")
        repo.setAvatarImageName("avatar_red")
        repo.setUsername("manu")

        val prefs = store.data.first()
        assertEquals(false, prefs[SettingsKeys.HAPTIC_ON_ERROR])
        assertEquals("light", prefs[SettingsKeys.APPEARANCE])
        assertEquals(3, prefs[SettingsKeys.MATH_SUBSTRACT.numberOfDigits])
        assertEquals(true, prefs[SettingsKeys.MATH_SUBSTRACT.useNegativeNumbers])
        assertEquals(setOf("articulo", "verbo"), prefs[SettingsKeys.SPANISH_ACTIVE_CATEGORIES])
        assertEquals(setOf("fisica"), prefs[SettingsKeys.TRIVIA_ACTIVE_CATEGORIES])
        assertEquals("easy", prefs[SettingsKeys.TRIVIA_DIFFICULTY])
        assertEquals("manu", prefs[SettingsKeys.PROFILE_USERNAME])

        val s = repo.settings.first()
        assertFalse(s.hapticOnSound)
        assertEquals(45, s.math.timeLimit)
        assertEquals(OperationSettings(3, true, false), s.math.substract)
        assertEquals(OperationSettings(), s.math.add)
        assertEquals(listOf(WordCategory.VERBO, WordCategory.ARTICULO), s.spanish.activeCategories)
        assertEquals(75, s.trivia.timeLimit)
        assertEquals("avatar_red", s.profile.avatarImageName)

        repo.setUsername(null)
        assertNull(repo.settings.first().profile.username)
    }

    @Test(expected = IllegalArgumentException::class)
    fun operationSettings_rejectsNonMathOperation() = runBlocking {
        val repo = DataStoreSettingsRepository(dataStore(File(tmp.root, "c.preferences_pb")))
        repo.setOperationSettings(GameOperation.TRIVIA, OperationSettings())
    }
}
