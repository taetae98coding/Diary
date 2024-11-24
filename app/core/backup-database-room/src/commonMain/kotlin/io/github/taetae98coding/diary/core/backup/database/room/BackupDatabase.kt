package io.github.taetae98coding.diary.core.backup.database.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.taetae98coding.diary.core.backup.database.room.dao.MemoBackupEntityDao
import io.github.taetae98coding.diary.core.backup.database.room.dao.TagBackupEntityDao
import io.github.taetae98coding.diary.core.backup.database.room.entity.MemoBackupEntity
import io.github.taetae98coding.diary.core.backup.database.room.entity.TagBackupEntity
import io.github.taetae98coding.diary.core.backup.database.room.internal.BackupDatabaseConstructor
import io.github.taetae98coding.diary.library.room.InstantConverter

@Database(
	entities = [
		MemoBackupEntity::class,
		TagBackupEntity::class,
	],
	version = 1,
)
@ConstructedBy(BackupDatabaseConstructor::class)
@TypeConverters(
	InstantConverter::class,
)
internal abstract class BackupDatabase : RoomDatabase() {
	abstract fun memo(): MemoBackupEntityDao

	abstract fun tag(): TagBackupEntityDao
}
