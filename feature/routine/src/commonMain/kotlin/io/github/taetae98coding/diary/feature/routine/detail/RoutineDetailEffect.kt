package io.github.taetae98coding.diary.feature.routine.detail

internal sealed class RoutineDetailEffect {
    data object None : RoutineDetailEffect()
    data object UpdateFinish : RoutineDetailEffect()
    data object DeleteFinish : RoutineDetailEffect()
    data object UnknownError : RoutineDetailEffect()
}
