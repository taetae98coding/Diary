package io.github.taetae98coding.diary.feature.routine.add.card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Stable
internal class RoutineCountCardState(initialCount: Int) {
    var count: Int by mutableIntStateOf(initialCount)
        private set

    fun increment() {
        count++
    }

    fun decrement() {
        count = (count - 1).coerceAtLeast(1)
    }

    fun clear() {
        count = 1
    }

    companion object {
        fun saver(): Saver<RoutineCountCardState, Any> = listSaver(
            save = { listOf(it.count) },
            restore = { RoutineCountCardState(initialCount = it[0]) },
        )
    }
}

@Composable
internal fun rememberRoutineCountCardState(
    vararg inputs: Any?,
    initialCount: Int = 1,
): RoutineCountCardState {
    return rememberSaveable(
        inputs = inputs,
        saver = RoutineCountCardState.saver(),
    ) {
        RoutineCountCardState(initialCount = initialCount)
    }
}
