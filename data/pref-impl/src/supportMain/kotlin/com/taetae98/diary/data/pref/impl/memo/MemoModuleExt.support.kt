package com.taetae98.diary.data.pref.impl.memo

import com.taetae98.diary.data.pref.api.MemoPrefDataSource
import com.taetae98.diary.data.pref.api.MemoTagPrefDataSource
import com.taetae98.diary.data.pref.impl.ext.getDataSource

internal actual fun MemoModule.getMemoPrefDataSource(): MemoPrefDataSource {
    return MemoPrefDataSourceImpl(
        dataStore = getDataSource("memo_datastore.preferences_pb"),
    )
}

internal actual fun MemoModule.getMemoTagPrefDataSource(): MemoTagPrefDataSource {
    return MemoTagPrefDataSourceImpl(
        dataStore = getDataSource("memo_tag_datastore.preferences_pb"),
    )
}
