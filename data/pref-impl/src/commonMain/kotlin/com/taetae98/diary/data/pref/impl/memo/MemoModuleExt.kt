package com.taetae98.diary.data.pref.impl.memo

import com.taetae98.diary.data.pref.api.MemoPrefDataSource
import com.taetae98.diary.data.pref.api.MemoTagPrefDataSource

internal expect fun MemoModule.getMemoPrefDataSource(): MemoPrefDataSource

internal expect fun MemoModule.getMemoTagPrefDataSource(): MemoTagPrefDataSource