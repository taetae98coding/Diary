package com.taetae98.diary.data.pref.impl.tag

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.taetae98.diary.data.pref.impl.ext.getDatastore
import okio.Path.Companion.toPath
import org.koin.core.component.inject

internal actual fun TagModule.getTagDataStore(): DataStore<Preferences> {
    val context by inject<Context>()

    return getDatastore {
        context.filesDir.resolve(TAG_DATA_STORE_FILE_NAME).absolutePath.toPath()
    }
}