package io.github.taetae98coding.diary.library.koin.room

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File
import org.koin.core.component.KoinComponent

public var koinRoomDefaultPath: String = System.getProperty("user.home")

public actual inline fun <reified T : RoomDatabase> KoinComponent.platformDatabaseBuilder(
    name: String,
): RoomDatabase.Builder<T> {
    runCatching { File("$koinRoomDefaultPath/$name").parentFile?.mkdirs() }
    return Room.databaseBuilder(name = "$koinRoomDefaultPath/$name")
}
