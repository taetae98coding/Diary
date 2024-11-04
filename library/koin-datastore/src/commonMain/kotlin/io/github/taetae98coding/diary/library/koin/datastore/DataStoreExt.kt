package io.github.taetae98coding.diary.library.koin.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath
import org.koin.core.component.KoinComponent

public fun KoinComponent.getDataStore(name: String): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = { getDataStoreAbsolutePath(name).toPath() },
    )
}

internal expect fun KoinComponent.getDataStoreAbsolutePath(name: String): String
