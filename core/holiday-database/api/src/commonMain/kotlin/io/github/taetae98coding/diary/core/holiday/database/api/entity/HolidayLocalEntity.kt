package io.github.taetae98coding.diary.core.holiday.database.api.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import kotlinx.datetime.LocalDate

@Entity(
    tableName = "Holiday",
    primaryKeys = ["name", "start", "endInclusive"],
)
public data class HolidayLocalEntity(
    @ColumnInfo(name = "name", defaultValue = "")
    val name: String,
    @ColumnInfo(name = "isHoliday", defaultValue = "0")
    val isHoliday: Boolean,
    @ColumnInfo(name = "start", defaultValue = "1998-01-09")
    val start: LocalDate,
    @ColumnInfo(name = "endInclusive", defaultValue = "1998-01-09")
    val endInclusive: LocalDate,
)
