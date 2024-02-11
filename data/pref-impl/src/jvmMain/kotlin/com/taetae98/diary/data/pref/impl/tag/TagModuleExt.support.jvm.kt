package com.taetae98.diary.data.pref.impl.tag

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.taetae98.diary.data.pref.impl.ext.getDatastore
import okio.Path.Companion.toPath

internal actual fun TagModule.getTagDataStore(): DataStore<Preferences> {
    return getDatastore { TAG_DATA_STORE_FILE_NAME.toPath() }
}