package com.taetae98.diary.data.pref.impl.memo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.component.KoinComponent

internal const val MEMO_DATA_STORE_FILE_NAME = "memo_datastore.preferences_pb"

internal expect fun KoinComponent.getMemoDataStore(): DataStore<Preferences>
