package io.github.taetae98coding.diary.library.koin.room

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.component.KoinComponent
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
public actual inline fun <reified T : RoomDatabase> KoinComponent.platformDatabaseBuilder(
    name: String,
): RoomDatabase.Builder<T> {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )?.also {
//        NSFileManager.defaultManager.removeItemAtURL(it, null)
    }

    return Room.databaseBuilder(name = "${documentDirectory?.path}/$name")
}
