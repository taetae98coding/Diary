package io.github.taetae98coding.diary.core.database.api.entity

import androidx.room3.ColumnInfo
import kotlinx.datetime.LocalDate

public data class RoutineDetailLocalEntity(
    @ColumnInfo(name = "title", defaultValue = "")
    val title: String,
    @ColumnInfo(name = "description", defaultValue = "")
    val description: String,
    @ColumnInfo(name = "start", defaultValue = "NULL")
    val start: LocalDate?,
    @ColumnInfo(name = "endInclusive", defaultValue = "NULL")
    val endInclusive: LocalDate?,
    @ColumnInfo(name = "color", defaultValue = "0")
    val color: Int,
    @ColumnInfo(name = "routineCount", defaultValue = "1")
    val routineCount: Int,
)
