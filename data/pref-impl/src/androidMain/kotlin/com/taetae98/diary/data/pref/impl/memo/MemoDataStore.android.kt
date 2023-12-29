package com.taetae98.diary.data.pref.impl.memo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.taetae98.diary.data.pref.impl.ext.getDatastore
import okio.Path.Companion.toPath
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal actual fun KoinComponent.getMemoDataStore(): DataStore<Preferences> {
    val context by inject<Context>()

    return getDatastore {
        context.filesDir.resolve(MEMO_DATA_STORE_FILE_NAME).absolutePath.toPath()
    }
}