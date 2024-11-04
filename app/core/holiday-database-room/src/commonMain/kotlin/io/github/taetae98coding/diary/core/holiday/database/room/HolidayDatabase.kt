package io.github.taetae98coding.diary.core.holiday.database.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.taetae98coding.diary.core.holiday.database.room.internal.HolidayDatabaseConstructor
import io.github.taetae98coding.diary.library.room.LocalDataConverter

@Database(
    entities = [HolidayEntity::class],
    version = 1,
)
@ConstructedBy(HolidayDatabaseConstructor::class)
@TypeConverters(LocalDataConverter::class)
internal abstract class HolidayDatabase : RoomDatabase() {
    abstract fun holidayDao(): HolidayEntityDao
}
