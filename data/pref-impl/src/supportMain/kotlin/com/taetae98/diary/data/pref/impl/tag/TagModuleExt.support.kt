package com.taetae98.diary.data.pref.impl.tag

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.taetae98.diary.data.pref.api.TagPrefDataSource

internal const val TAG_DATA_STORE_FILE_NAME = "tag_datastore.preferences_pb"

internal actual fun TagModule.getTagPrefDataSource(): TagPrefDataSource {
    return TagPrefDataSourceImpl(
        dataStore = getTagDataStore(),
    )
}

internal expect fun TagModule.getTagDataStore(): DataStore<Preferences>