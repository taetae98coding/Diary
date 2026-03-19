package io.github.taetae98coding.diary.core.database.api.entity

import androidx.room3.ColumnInfo
import kotlinx.datetime.LocalDateTime

public data class MemoDetailLocalEntity(
    @ColumnInfo(name = "title", defaultValue = "")
    val title: String,
    @ColumnInfo(name = "description", defaultValue = "")
    val description: String,
    @ColumnInfo(name = "isAllDay", defaultValue = "0")
    val isAllDay: Boolean,
    @ColumnInfo(name = "start", defaultValue = "NULL")
    val start: LocalDateTime?,
    @ColumnInfo(name = "endInclusive", defaultValue = "NULL")
    val endInclusive: LocalDateTime?,
    @ColumnInfo(name = "color", defaultValue = "0")
    val color: Int,
)
