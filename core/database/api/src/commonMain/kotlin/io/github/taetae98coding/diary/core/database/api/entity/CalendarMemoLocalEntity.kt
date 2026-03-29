package io.github.taetae98coding.diary.core.database.api.entity

import androidx.room3.ColumnInfo
import kotlin.uuid.Uuid
import kotlinx.datetime.LocalDateTime

public data class CalendarMemoLocalEntity(
    @ColumnInfo(name = "id", defaultValue = "00000000-0000-0000-0000-000000000000")
    val id: Uuid,
    @ColumnInfo(name = "title", defaultValue = "")
    val title: String,
    @ColumnInfo(name = "isAllDay", defaultValue = "0")
    val isAllDay: Boolean,
    @ColumnInfo(name = "start", defaultValue = "1998-01-09T00:00")
    val start: LocalDateTime,
    @ColumnInfo(name = "endInclusive", defaultValue = "1998-01-09T00:00")
    val endInclusive: LocalDateTime,
    @ColumnInfo(name = "calendarMemoColor", defaultValue = "")
    val color: Int,
)
