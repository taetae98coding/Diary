package com.taetae98.diary.data.pref.impl.ext

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path
import org.koin.core.component.KoinComponent

internal fun getDatastore(produceFile: () -> Path): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = produceFile
    )
}

internal expect fun KoinComponent.getDataSource(fileName: String): DataStore<Preferences>