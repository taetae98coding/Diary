package com.taetae98.diary.data.pref.impl.memo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.taetae98.diary.data.pref.impl.ext.getDatastore
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
internal actual fun MemoModule.getMemoDataStore(): DataStore<Preferences> {
    return getDatastore {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )

        "${documentDirectory?.path}/$MEMO_DATA_STORE_FILE_NAME".toPath()
    }
}