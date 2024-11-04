package io.github.taetae98coding.diary.core.holiday.database.room.internal

import androidx.room.RoomDatabaseConstructor
import io.github.taetae98coding.diary.core.holiday.database.room.HolidayDatabase

internal expect object HolidayDatabaseConstructor : RoomDatabaseConstructor<HolidayDatabase> {
    override fun initialize(): HolidayDatabase
}
