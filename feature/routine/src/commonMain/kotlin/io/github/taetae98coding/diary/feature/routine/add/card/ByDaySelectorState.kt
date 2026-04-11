package io.github.taetae98coding.diary.feature.routine.add.card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import kotlinx.datetime.DayOfWeek

@Stable
internal class ByDaySelectorState {
    private val _selectedDaysOfWeek = mutableStateSetOf<DayOfWeek>()
    val selectedDaysOfWeek: Set<DayOfWeek>
        get() = _selectedDaysOfWeek

    var selectedOrdinal: Int? by mutableStateOf(null)
        private set

    var expanded: Boolean by mutableStateOf(false)
        private set

    fun addDayOfWeek(value: DayOfWeek) {
        _selectedDaysOfWeek += value
    }

    fun removeDayOfWeek(value: DayOfWeek) {
        _selectedDaysOfWeek -= value
    }

    fun updateSelectedOrdinal(value: Int?) {
        selectedOrdinal = value
        expanded = false
    }

    fun updateExpanded(value: Boolean) {
        expanded = value
    }

    fun clear() {
        _selectedDaysOfWeek.clear()
    }
}

@Composable
internal fun rememberByDaySelectorState(): ByDaySelectorState {
    return retain { ByDaySelectorState() }
}
