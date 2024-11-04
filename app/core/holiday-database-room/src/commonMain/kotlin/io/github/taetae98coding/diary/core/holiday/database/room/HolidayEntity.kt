package io.github.taetae98coding.diary.core.holiday.database.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.datetime.LocalDate

@Entity(
    primaryKeys = ["name", "start", "endInclusive"],
)
internal data class HolidayEntity(
    @ColumnInfo(defaultValue = "")
    val name: String,
    @ColumnInfo(defaultValue = "1900-01-01")
    override val start: LocalDate,
    @ColumnInfo(defaultValue = "1900-01-01")
    override val endInclusive: LocalDate,
) : ClosedRange<LocalDate>
