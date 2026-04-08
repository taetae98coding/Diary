package io.github.taetae98coding.diary.feature.routine.add.card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue

@Stable
internal class ByMonthDaySelectorState {
    var day: Int by mutableIntStateOf(1)
        private set

    var expanded: Boolean by mutableStateOf(false)
        private set

    fun updateDay(value: Int) {
        day = value
        expanded = false
    }

    fun updateExpanded(value: Boolean) {
        expanded = value
    }
}

@Composable
internal fun rememberByMonthDaySelectorState(): ByMonthDaySelectorState {
    return retain { ByMonthDaySelectorState() }
}
