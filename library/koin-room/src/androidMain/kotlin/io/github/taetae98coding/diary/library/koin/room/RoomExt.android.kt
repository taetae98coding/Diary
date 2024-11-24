package io.github.taetae98coding.diary.library.koin.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

public actual inline fun <reified T : RoomDatabase> KoinComponent.platformDatabaseBuilder(
	name: String,
): RoomDatabase.Builder<T> {
	val context by inject<Context>()
	val file = context.getDatabasePath(name)

	return Room.databaseBuilder(context, file.absolutePath)
}
