package io.github.taetae98coding.diary.feature.routine.add

internal sealed class RoutineAddEffect {
    data object None : RoutineAddEffect()
    data object AddFinish : RoutineAddEffect()
    data object TitleBlank : RoutineAddEffect()
    data object RRulesEmpty : RoutineAddEffect()
    data object UnknownError : RoutineAddEffect()
}
