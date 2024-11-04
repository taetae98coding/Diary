package io.github.taetae98coding.diary.core.diary.database.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.taetae98coding.diary.core.diary.database.room.dao.MemoBackupEntityDao
import io.github.taetae98coding.diary.core.diary.database.room.dao.MemoEntityDao
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoBackupEntity
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoEntity
import io.github.taetae98coding.diary.core.diary.database.room.internal.DiaryDatabaseConstructor
import io.github.taetae98coding.diary.library.room.InstantConverter
import io.github.taetae98coding.diary.library.room.LocalDataConverter

@Database(
    entities = [
        MemoEntity::class,
        MemoBackupEntity::class,
    ],
    version = 1
)
@ConstructedBy(DiaryDatabaseConstructor::class)
@TypeConverters(
    LocalDataConverter::class,
    InstantConverter::class,
)
internal abstract class DiaryDatabase : RoomDatabase() {
    abstract fun memo(): MemoEntityDao
    abstract fun memoBackup(): MemoBackupEntityDao
}
