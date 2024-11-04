package io.github.taetae98coding.diary.library.koin.datastore

import android.content.Context
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal actual fun KoinComponent.getDataStoreAbsolutePath(name: String): String {
    val context by inject<Context>()

    return context.filesDir.resolve(name).absolutePath
}
