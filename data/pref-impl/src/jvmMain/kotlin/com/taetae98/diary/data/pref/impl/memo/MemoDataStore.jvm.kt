package com.taetae98.diary.data.pref.impl.memo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.taetae98.diary.data.pref.impl.ext.getDatastore
import okio.Path.Companion.toPath
import org.koin.core.component.KoinComponent

internal actual fun KoinComponent.getMemoDataStore(): DataStore<Preferences> {
    return getDatastore { MEMO_DATA_STORE_FILE_NAME.toPath() }
}