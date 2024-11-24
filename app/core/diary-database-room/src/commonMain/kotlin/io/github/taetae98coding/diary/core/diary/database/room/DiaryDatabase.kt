package io.github.taetae98coding.diary.core.diary.database.room

import androidx.room.AutoMigration
import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.DeleteTable
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import io.github.taetae98coding.diary.core.diary.database.room.dao.MemoEntityDao
import io.github.taetae98coding.diary.core.diary.database.room.dao.MemoTagEntityDao
import io.github.taetae98coding.diary.core.diary.database.room.dao.TagEntityDao
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoEntity
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoTagEntity
import io.github.taetae98coding.diary.core.diary.database.room.entity.TagEntity
import io.github.taetae98coding.diary.core.diary.database.room.internal.DiaryDatabaseConstructor
import io.github.taetae98coding.diary.library.room.InstantConverter
import io.github.taetae98coding.diary.library.room.LocalDataConverter

@Database(
    entities = [
        MemoEntity::class,
        TagEntity::class,
        MemoTagEntity::class,
    ],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2, DiaryDatabase.DeleteMemoBackupEntity1To2::class)
    ]
)
@ConstructedBy(DiaryDatabaseConstructor::class)
@TypeConverters(
    LocalDataConverter::class,
    InstantConverter::class,
)
internal abstract class DiaryDatabase : RoomDatabase() {
    @DeleteTable("MemoBackupEntity")
    class DeleteMemoBackupEntity1To2 : AutoMigrationSpec

    abstract fun memo(): MemoEntityDao
    abstract fun tag(): TagEntityDao
    abstract fun memoTag(): MemoTagEntityDao
}
