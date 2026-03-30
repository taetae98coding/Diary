package io.github.taetae98coding.diary.core.database.impl

import androidx.room3.AutoMigration
import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.TypeConverters
import io.github.taetae98coding.diary.core.database.api.entity.AccountMemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.AccountTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.CalendarMemoFilterTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.ListMemoFilterTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.MemoTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncMemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncMemoTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.TagLocalEntity
import io.github.taetae98coding.diary.core.database.impl.converter.SyncStateTypeConverter
import io.github.taetae98coding.diary.core.database.impl.dao.AccountCalendarMemoDao
import io.github.taetae98coding.diary.core.database.impl.dao.AccountCalendarMemoFilterTagDao
import io.github.taetae98coding.diary.core.database.impl.dao.AccountListMemoDao
import io.github.taetae98coding.diary.core.database.impl.dao.AccountListMemoFilterTagDao
import io.github.taetae98coding.diary.core.database.impl.dao.AccountMemoDao
import io.github.taetae98coding.diary.core.database.impl.dao.AccountTagDao
import io.github.taetae98coding.diary.core.database.impl.dao.CalendarMemoFilterTagDao
import io.github.taetae98coding.diary.core.database.impl.dao.ListMemoFilterTagDao
import io.github.taetae98coding.diary.core.database.impl.dao.MemoDao
import io.github.taetae98coding.diary.core.database.impl.dao.MemoTagDao
import io.github.taetae98coding.diary.core.database.impl.dao.SyncMemoDao
import io.github.taetae98coding.diary.core.database.impl.dao.SyncMemoTagDao
import io.github.taetae98coding.diary.core.database.impl.dao.SyncTagDao
import io.github.taetae98coding.diary.core.database.impl.dao.TagDao
import io.github.taetae98coding.diary.library.room.common.converter.LocalDateTimeTypeConverter
import io.github.taetae98coding.diary.library.room.common.converter.UuidTypeConverter

@Database(
    entities = [
        MemoLocalEntity::class,
        TagLocalEntity::class,
        MemoTagLocalEntity::class,
        AccountMemoLocalEntity::class,
        AccountTagLocalEntity::class,
        SyncMemoLocalEntity::class,
        SyncTagLocalEntity::class,
        SyncMemoTagLocalEntity::class,
        ListMemoFilterTagLocalEntity::class,
        CalendarMemoFilterTagLocalEntity::class,
    ],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
    ],
)
@TypeConverters(
    UuidTypeConverter::class,
    LocalDateTimeTypeConverter::class,
    SyncStateTypeConverter::class,
)
@ConstructedBy(DiaryDatabaseConstructor::class)
internal abstract class DiaryDatabase : RoomDatabase() {
    abstract fun accountCalendarMemoDao(): AccountCalendarMemoDao
    abstract fun accountListMemoDao(): AccountListMemoDao
    abstract fun accountMemoDao(): AccountMemoDao
    abstract fun accountTagDao(): AccountTagDao
    abstract fun accountListMemoFilterTagDao(): AccountListMemoFilterTagDao
    abstract fun accountCalendarMemoFilterTagDao(): AccountCalendarMemoFilterTagDao

    abstract fun memoDao(): MemoDao
    abstract fun memoTagDao(): MemoTagDao
    abstract fun tagDao(): TagDao

    abstract fun syncMemoDao(): SyncMemoDao
    abstract fun syncMemoTagDao(): SyncMemoTagDao
    abstract fun syncTagDao(): SyncTagDao

    abstract fun listMemoFilterTagDao(): ListMemoFilterTagDao
    abstract fun calendarMemoFilterTagDao(): CalendarMemoFilterTagDao
}
