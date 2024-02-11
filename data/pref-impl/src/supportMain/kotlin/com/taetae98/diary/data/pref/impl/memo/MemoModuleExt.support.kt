package com.taetae98.diary.data.pref.impl.memo

import com.taetae98.diary.data.pref.api.MemoPrefDataSource
import com.taetae98.diary.data.pref.impl.ext.getDataSource

internal actual fun MemoModule.getMemoPrefDataSource(): MemoPrefDataSource {
    return MemoPrefDataSourceImpl(
        dataStore = getDataSource("memo_datastore.preferences_pb"),
    )
}
