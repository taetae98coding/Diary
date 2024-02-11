package com.taetae98.diary.data.pref.impl.ext

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal actual fun KoinComponent.getDataSource(fileName: String): DataStore<Preferences> {
    val context by inject<Context>()

    return getDatastore {
        context.filesDir.resolve(fileName).absolutePath.toPath()
    }
}