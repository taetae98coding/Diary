package io.github.taetae98coding.diary.core.filter.database.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.taetae98coding.diary.core.filter.database.room.dao.CalendarFilterEntityDao
import io.github.taetae98coding.diary.core.filter.database.room.entity.CalendarFilterEntity
import io.github.taetae98coding.diary.core.filter.database.room.internal.FilterDatabaseConstructor

@Database(
	entities = [
		CalendarFilterEntity::class,
	],
	version = 1,
)
@ConstructedBy(FilterDatabaseConstructor::class)
internal abstract class FilterDatabase : RoomDatabase() {
	abstract fun calendar(): CalendarFilterEntityDao
}
