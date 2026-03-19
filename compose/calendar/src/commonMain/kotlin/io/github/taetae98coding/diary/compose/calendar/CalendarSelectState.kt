package io.github.taetae98coding.diary.compose.calendar

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateRange

@Stable
public class CalendarSelectState {
    public var localDateRange: LocalDateRange? by mutableStateOf(null)
        private set

    public fun select(value: LocalDateRange) {
        localDateRange = value
    }

    public fun unselect() {
        localDateRange = null
    }
}
