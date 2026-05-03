package io.github.taetae98coding.diary.feature.routine.detail

internal sealed interface RoutineDetailEffect {
    data object None : RoutineDetailEffect

    data object UpdateFinish : RoutineDetailEffect

    data object UnknownError : RoutineDetailEffect
}
