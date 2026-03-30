package io.github.taetae98coding.diary.core.holiday.database.impl

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.TypeConverters
import io.github.taetae98coding.diary.core.holiday.database.api.entity.HolidayLocalEntity
import io.github.taetae98coding.diary.core.holiday.database.impl.dao.HolidayDao
import io.github.taetae98coding.diary.library.room.common.converter.LocalDateTypeConverter

@Database(
    entities = [
        HolidayLocalEntity::class,
    ],
    version = 1,
)
@TypeConverters(
    LocalDateTypeConverter::class,
)
@ConstructedBy(HolidayDatabaseConstructor::class)
internal abstract class HolidayDatabase : RoomDatabase() {
    abstract fun holidayDao(): HolidayDao
}
