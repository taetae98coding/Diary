package com.taetae98.diary.data.pref.impl.memo

import com.taetae98.diary.data.pref.api.MemoPrefDataSource

internal actual fun MemoModule.getMemoPrefDataSource(): MemoPrefDataSource {
    return MemoPrefDataSourceImpl()
}