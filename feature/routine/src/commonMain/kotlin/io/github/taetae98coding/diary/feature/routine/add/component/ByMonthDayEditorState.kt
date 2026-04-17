package io.github.taetae98coding.diary.feature.routine.add.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Stable
internal class ByMonthDayEditorState(initialByMonthDay: Set<Int> = emptySet()) {
    var byMonthDay: Set<Int> by mutableStateOf(initialByMonthDay)
        private set

    val hasLastDay: Boolean
        get() = LAST_DAY_CODE in byMonthDay

    fun updateByMonthDay(value: Set<Int>) {
        byMonthDay = value
    }

    fun selectDay(day: Int) {
        byMonthDay = byMonthDay + day
    }

    fun unselectDay(day: Int) {
        byMonthDay = byMonthDay - day
    }

    fun selectLastDay() {
        byMonthDay = byMonthDay + LAST_DAY_CODE
    }

    fun unselectLastDay() {
        byMonthDay = byMonthDay - LAST_DAY_CODE
    }

    companion object {
        const val LAST_DAY_CODE: Int = -1

        fun saver(): Saver<ByMonthDayEditorState, Any> = listSaver(
            save = { it.byMonthDay.toList() },
            restore = { ByMonthDayEditorState(initialByMonthDay = it.toSet()) },
        )
    }
}

@Composable
internal fun rememberByMonthDayEditorState(initialByMonthDay: Set<Int> = emptySet()): ByMonthDayEditorState {
    return rememberSaveable(saver = ByMonthDayEditorState.saver()) {
        ByMonthDayEditorState(initialByMonthDay)
    }
}
