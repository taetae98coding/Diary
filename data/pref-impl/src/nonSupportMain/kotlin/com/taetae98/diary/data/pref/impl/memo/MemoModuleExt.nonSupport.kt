package com.taetae98.diary.data.pref.impl.memo

import com.taetae98.diary.data.pref.api.MemoPrefDataSource
import com.taetae98.diary.data.pref.api.MemoTagPrefDataSource

internal actual fun MemoModule.getMemoPrefDataSource(): MemoPrefDataSource {
    return MemoPrefDataSourceImpl()
}

internal actual fun MemoModule.getMemoTagPrefDataSource(): MemoTagPrefDataSource {
    return MemoTagPrefDataSourceImpl()
}