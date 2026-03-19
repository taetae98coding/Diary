package io.github.taetae98coding.diary.compose.core.date

import androidx.compose.material3.DateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import io.github.taetae98coding.diary.compose.core.internal.toEpochMillis
import io.github.taetae98coding.diary.compose.core.internal.toLocalDate
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateRange

@Stable
public class DiaryDateRangePickerState(internal val materialState: DateRangePickerState) {
    public val selectedStartDate: LocalDate?
        get() = materialState.selectedStartDateMillis?.toLocalDate()

    public val selectedEndDate: LocalDate?
        get() = materialState.selectedEndDateMillis?.toLocalDate()

    public val selectedLocalDateRange: LocalDateRange?
        get() {
            val start = selectedStartDate ?: return null
            val endInclusive = selectedEndDate ?: return null
            return start..endInclusive
        }
}

@Composable
public fun rememberDiaryDateRangePickerState(initialSelectedLocalDateRange: LocalDateRange? = null): DiaryDateRangePickerState {
    val materialState = androidx.compose.material3.rememberDateRangePickerState(
        initialSelectedStartDateMillis = initialSelectedLocalDateRange?.start?.toEpochMillis(),
        initialSelectedEndDateMillis = initialSelectedLocalDateRange?.endInclusive?.toEpochMillis(),
    )

    return remember(materialState) {
        DiaryDateRangePickerState(
            materialState = materialState,
        )
    }
}
