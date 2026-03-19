package io.github.taetae98coding.diary.compose.core.date

import androidx.compose.material3.DatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import io.github.taetae98coding.diary.compose.core.internal.toEpochMillis
import io.github.taetae98coding.diary.compose.core.internal.toLocalDate
import kotlin.time.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

@Stable
public class LocalDatePickerState(internal val materialState: DatePickerState) {
    public val selectedDate: LocalDate?
        get() = materialState.selectedDateMillis?.toLocalDate()
}

@Composable
public fun rememberLocalDatePickerState(initialSelectedDate: LocalDate? = remember { Clock.System.todayIn(TimeZone.currentSystemDefault()) }): LocalDatePickerState {
    val materialState = androidx.compose.material3.rememberDatePickerState(
        initialSelectedDateMillis = initialSelectedDate?.toEpochMillis(),
    )

    return remember(materialState) {
        LocalDatePickerState(materialState = materialState)
    }
}
