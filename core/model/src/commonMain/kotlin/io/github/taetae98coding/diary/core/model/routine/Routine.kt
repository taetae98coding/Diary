package io.github.taetae98coding.diary.core.model.routine

import kotlin.uuid.Uuid
import kotlinx.datetime.LocalDate

public data class Routine(
    val id: Uuid,
    val detail: RoutineDetail,
    val rRules: List<RoutineRRule>,
    val rDates: Set<LocalDate>,
    val exDates: Set<LocalDate>,
    val isFinished: Boolean,
    val isDeleted: Boolean,
    val updatedAt: Long,
    val createdAt: Long,
) {
    public fun isActive(localDate: LocalDate): Boolean {
        if (localDate in rDates) return true
        if (localDate in exDates) return false

        return rRules.any { it.isActive(localDate) }
    }
}
