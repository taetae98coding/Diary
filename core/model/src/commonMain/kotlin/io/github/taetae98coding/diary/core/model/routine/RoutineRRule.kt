package io.github.taetae98coding.diary.core.model.routine

public data class RoutineRRule(
    val diaryByDay: RRuleDiaryByDay = RRuleDiaryByDay(),
    val byMonthDay: Set<Int> = emptySet(),
)
