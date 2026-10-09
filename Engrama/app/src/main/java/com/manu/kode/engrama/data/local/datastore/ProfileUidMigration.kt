package com.manu.kode.engrama.data.local.datastore

import androidx.datastore.core.DataMigration
import androidx.datastore.preferences.core.Preferences
import java.util.UUID

/** Genera y persiste profile.uid una sola vez, antes de la primera lectura del DataStore. */
class ProfileUidMigration(
    private val newUid: () -> String = { UUID.randomUUID().toString() }
) : DataMigration<Preferences> {

    override suspend fun shouldMigrate(currentData: Preferences): Boolean =
        currentData[SettingsKeys.PROFILE_UID].isNullOrEmpty()

    override suspend fun migrate(currentData: Preferences): Preferences =
        currentData.toMutablePreferences().apply {
            this[SettingsKeys.PROFILE_UID] = newUid()
        }.toPreferences()

    override suspend fun cleanUp() = Unit
}
