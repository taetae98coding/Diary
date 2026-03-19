package io.github.taetae98coding.diary.core.database.impl

import androidx.room3.AutoMigration
import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.TypeConverters
import io.github.taetae98coding.diary.core.database.api.entity.AccountMemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncMemoLocalEntity
import io.github.taetae98coding.diary.core.database.impl.converter.SyncStateTypeConverter
import io.github.taetae98coding.diary.core.database.impl.dao.AccountCalendarMemoDao
import io.github.taetae98coding.diary.core.database.impl.dao.AccountMemoDao
import io.github.taetae98coding.diary.core.database.impl.dao.MemoDao
import io.github.taetae98coding.diary.core.database.impl.dao.SyncMemoDao
import io.github.taetae98coding.diary.library.room.common.converter.LocalDateTimeTypeConverter
import io.github.taetae98coding.diary.library.room.common.converter.UuidTypeConverter

@Database(
    entities = [
        AccountMemoLocalEntity::class,
        MemoLocalEntity::class,
        SyncMemoLocalEntity::class,
    ],
    version = 1,
)
@TypeConverters(
    UuidTypeConverter::class,
    LocalDateTimeTypeConverter::class,
    SyncStateTypeConverter::class,
)
@ConstructedBy(DiaryDatabaseConstructor::class)
internal abstract class DiaryDatabase : RoomDatabase() {
    abstract fun accountCalendarMemoDao(): AccountCalendarMemoDao
    abstract fun accountMemoDao(): AccountMemoDao
    abstract fun memoDao(): MemoDao
    abstract fun syncMemoDao(): SyncMemoDao
}
