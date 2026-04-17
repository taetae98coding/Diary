package io.github.taetae98coding.diary.feature.routine.add.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Stable
internal class RoutineCountEditorState(initialCount: Int = 1) {
    var count: Int by mutableIntStateOf(initialCount.coerceAtLeast(1))
        private set

    fun updateCount(value: Int) {
        count = value.coerceAtLeast(1)
    }

    fun increment() {
        updateCount(count + 1)
    }

    fun decrement() {
        updateCount(count - 1)
    }

    companion object {
        fun saver(): Saver<RoutineCountEditorState, Int> = Saver(
            save = { it.count },
            restore = { RoutineCountEditorState(initialCount = it) },
        )
    }
}

@Composable
internal fun rememberRoutineCountEditorState(
    vararg inputs: Any?,
    initialCount: Int = 1,
): RoutineCountEditorState {
    return rememberSaveable(
        inputs = inputs,
        saver = RoutineCountEditorState.saver(),
    ) {
        RoutineCountEditorState(initialCount)
    }
}
