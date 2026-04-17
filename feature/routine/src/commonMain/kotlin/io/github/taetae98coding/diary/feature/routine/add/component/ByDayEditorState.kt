package io.github.taetae98coding.diary.feature.routine.add.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import io.github.taetae98coding.diary.core.model.routine.RRuleDiaryByDay
import kotlinx.datetime.DayOfWeek

@Stable
internal class ByDayEditorState(initialDiaryByDay: RRuleDiaryByDay = RRuleDiaryByDay()) {
    var selectedDays: Set<DayOfWeek> by mutableStateOf(initialDiaryByDay.days)
        private set

    var ordinal: Int? by mutableStateOf(initialDiaryByDay.ordinal)
        private set

    val diaryByDay: RRuleDiaryByDay
        get() = RRuleDiaryByDay(days = selectedDays, ordinal = ordinal)

    fun selectDay(day: DayOfWeek) {
        selectedDays = selectedDays + day
    }

    fun unselectDay(day: DayOfWeek) {
        selectedDays = selectedDays - day
    }

    fun updateOrdinal(value: Int?) {
        ordinal = value
    }

    fun updateDiaryByDay(value: RRuleDiaryByDay) {
        selectedDays = value.days
        ordinal = value.ordinal
    }

    companion object {
        fun saver(): Saver<ByDayEditorState, Any> = listSaver(
            save = { state ->
                listOf(
                    state.selectedDays.map { it.ordinal },
                    state.ordinal,
                )
            },
            restore = { list ->
                val days = (list[0] as? List<*>).orEmpty()
                    .mapNotNull { ordinal -> (ordinal as? Int)?.let { DayOfWeek.entries[it] } }
                    .toSet()
                val ordinal = list[1] as? Int

                ByDayEditorState(initialDiaryByDay = RRuleDiaryByDay(days = days, ordinal = ordinal))
            },
        )
    }
}

@Composable
internal fun rememberByDayEditorState(initialDiaryByDay: RRuleDiaryByDay = RRuleDiaryByDay()): ByDayEditorState {
    return rememberSaveable(saver = ByDayEditorState.saver()) {
        ByDayEditorState(initialDiaryByDay)
    }
}
