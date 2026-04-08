package io.github.taetae98coding.diary.core.model.routine

import kotlinx.datetime.LocalDate

public data class RoutineDetail(
    val title: String,
    val description: String,
    val start: LocalDate?,
    val endInclusive: LocalDate?,
    val color: Int,
    val routineCount: Int,
)
