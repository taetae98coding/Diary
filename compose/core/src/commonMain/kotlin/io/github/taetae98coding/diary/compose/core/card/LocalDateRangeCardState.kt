package io.github.taetae98coding.diary.compose.core.card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.datetime.LocalDate

@Stable
public class LocalDateRangeCardState(
    initialStart: LocalDate?,
    initialEndInclusive: LocalDate?,
) {
    public var start: LocalDate? by mutableStateOf(initialStart)
        private set
    public var endInclusive: LocalDate? by mutableStateOf(initialEndInclusive)
        private set

    public fun updateStart(value: LocalDate?) {
        start = value
        val notNullEndInclusive = endInclusive ?: return
        if (value != null && value > notNullEndInclusive) {
            endInclusive = value
        }
    }

    public fun updateEndInclusive(value: LocalDate?) {
        endInclusive = value
        val notNullStart = start ?: return
        if (value != null && notNullStart > value) {
            start = value
        }
    }

    public companion object {
        public fun saver(): Saver<LocalDateRangeCardState, Any> = listSaver(
            save = {
                listOf(
                    it.start?.toEpochDays(),
                    it.endInclusive?.toEpochDays(),
                )
            },
            restore = {
                LocalDateRangeCardState(
                    initialStart = it[0]?.let(LocalDate::fromEpochDays),
                    initialEndInclusive = it[1]?.let(LocalDate::fromEpochDays),
                )
            },
        )
    }
}

@Composable
public fun rememberLocalDateRangeCardState(
    vararg inputs: Any?,
    initialStart: LocalDate? = null,
    initialEndInclusive: LocalDate? = null,
): LocalDateRangeCardState {
    return rememberSaveable(
        inputs = inputs,
        saver = LocalDateRangeCardState.saver(),
    ) {
        LocalDateRangeCardState(
            initialStart = initialStart,
            initialEndInclusive = initialEndInclusive,
        )
    }
}
