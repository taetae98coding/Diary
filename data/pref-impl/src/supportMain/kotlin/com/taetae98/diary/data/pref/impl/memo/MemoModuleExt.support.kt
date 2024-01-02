package com.taetae98.diary.data.pref.impl.memo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.taetae98.diary.data.pref.api.MemoPrefDataSource

internal const val MEMO_DATA_STORE_FILE_NAME = "memo_datastore.preferences_pb"

internal actual fun MemoModule.getMemoPrefDataSource(): MemoPrefDataSource {
    return MemoPrefDataSourceImpl(
        dataStore = getMemoDataStore(),
    )
}

internal expect fun MemoModule.getMemoDataStore(): DataStore<Preferences>