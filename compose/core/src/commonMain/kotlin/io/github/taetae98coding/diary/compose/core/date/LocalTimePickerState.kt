package io.github.taetae98coding.diary.compose.core.date

import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import kotlin.time.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Stable
public class LocalTimePickerState(internal val materialState: TimePickerState) {
    public val selectedTime: LocalTime
        get() = LocalTime(materialState.hour, materialState.minute)
}

@Composable
public fun rememberLocalTimePickerState(
    initialSelectedTime: LocalTime = remember {
        Clock.System
            .now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .time
    },
): LocalTimePickerState {
    val materialState = rememberTimePickerState(
        initialHour = initialSelectedTime.hour,
        initialMinute = initialSelectedTime.minute,
    )

    return remember(materialState) {
        LocalTimePickerState(materialState = materialState)
    }
}
