package com.manu.kode.engrama.data.local.datastore

import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.mutablePreferencesOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProfileUidMigrationTest {

    private val migration = ProfileUidMigration(newUid = { "uid-generado" })

    @Test
    fun missingUid_isGenerated() = runTest {
        assertTrue(migration.shouldMigrate(emptyPreferences()))
        val migrated = migration.migrate(emptyPreferences())
        assertEquals("uid-generado", migrated[SettingsKeys.PROFILE_UID])
    }

    @Test
    fun existingUid_isKept() = runTest {
        val prefs = mutablePreferencesOf(SettingsKeys.PROFILE_UID to "uid-existente")
        assertFalse(migration.shouldMigrate(prefs))
    }
}
