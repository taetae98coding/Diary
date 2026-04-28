package io.github.taetae98coding.diary.feature.routine.add.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Stable
internal class CalendarVisibilityCardState(initialVisible: Boolean = true) {
    var isVisible: Boolean by mutableStateOf(initialVisible)
        private set

    fun update(value: Boolean) {
        isVisible = value
    }

    fun toggle() {
        update(!isVisible)
    }

    companion object {
        fun saver(): Saver<CalendarVisibilityCardState, Boolean> = Saver(
            save = { it.isVisible },
            restore = { CalendarVisibilityCardState(initialVisible = it) },
        )
    }
}

@Composable
internal fun rememberCalendarVisibilityCardState(
    vararg inputs: Any?,
    initialVisible: Boolean = true,
): CalendarVisibilityCardState {
    return rememberSaveable(
        inputs = inputs,
        saver = CalendarVisibilityCardState.saver(),
    ) {
        CalendarVisibilityCardState(initialVisible)
    }
}
