package io.github.taetae98coding.diary.library.koin.room

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.koin.core.component.KoinComponent

public inline fun <reified T : RoomDatabase> KoinComponent.getDatabaseBuilder(
    name: String,
): RoomDatabase.Builder<T> {
    return platformDatabaseBuilder<T>(name)
        .setDriver(BundledSQLiteDriver())
}

public expect inline fun <reified T : RoomDatabase> KoinComponent.platformDatabaseBuilder(
    name: String,
): RoomDatabase.Builder<T>
