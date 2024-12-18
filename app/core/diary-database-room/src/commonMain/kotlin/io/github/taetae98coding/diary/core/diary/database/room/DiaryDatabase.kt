package io.github.taetae98coding.diary.core.diary.database.room

import androidx.room.AutoMigration
import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.taetae98coding.diary.core.diary.database.room.dao.MemoBuddyEntityDao
import io.github.taetae98coding.diary.core.diary.database.room.dao.MemoEntityDao
import io.github.taetae98coding.diary.core.diary.database.room.dao.MemoTagEntityDao
import io.github.taetae98coding.diary.core.diary.database.room.dao.TagEntityDao
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoBuddyGroupEntity
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoEntity
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoTagEntity
import io.github.taetae98coding.diary.core.diary.database.room.entity.TagEntity
import io.github.taetae98coding.diary.core.diary.database.room.internal.DiaryDatabaseConstructor
import io.github.taetae98coding.diary.core.diary.database.room.migration.AutoMigration1To2
import io.github.taetae98coding.diary.library.room.InstantConverter
import io.github.taetae98coding.diary.library.room.LocalDataConverter

@Database(
	entities = [
		MemoEntity::class,
		TagEntity::class,
		MemoTagEntity::class,
		MemoBuddyGroupEntity::class,
	],
	version = 3,
	autoMigrations = [
		AutoMigration(from = 1, to = 2, AutoMigration1To2::class),
		AutoMigration(from = 2, to = 3),
	],
)
@ConstructedBy(DiaryDatabaseConstructor::class)
@TypeConverters(
	LocalDataConverter::class,
	InstantConverter::class,
)
internal abstract class DiaryDatabase : RoomDatabase() {
	abstract fun memo(): MemoEntityDao

	abstract fun tag(): TagEntityDao

	abstract fun memoTag(): MemoTagEntityDao

	abstract fun memoBuddyGroup(): MemoBuddyEntityDao
}
