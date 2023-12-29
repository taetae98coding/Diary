package com.taetae98.diary.data.pref.impl.ext

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path

internal fun getDatastore(produceFile: () -> Path): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = produceFile
    )
}