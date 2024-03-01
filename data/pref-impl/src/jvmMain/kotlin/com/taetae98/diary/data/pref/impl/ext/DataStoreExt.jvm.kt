package com.taetae98.diary.data.pref.impl.ext

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath
import org.koin.core.component.KoinComponent

internal actual fun KoinComponent.getDataSource(fileName: String): DataStore<Preferences> {
    return getDatastore { fileName.toPath() }
}