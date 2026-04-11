package io.github.taetae98coding.diary.feature.routine.add.card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.datetime.LocalDate

@Stable
internal class RoutineDateRangeCardState(
    initialStart: LocalDate?,
    initialEndInclusive: LocalDate?,
) {
    var start: LocalDate? by mutableStateOf(initialStart)
        private set

    var endInclusive: LocalDate? by mutableStateOf(initialEndInclusive)
        private set

    fun updateStart(value: LocalDate?) {
        start = value
        val currentEndInclusive = endInclusive
        if (value != null && currentEndInclusive != null && value > currentEndInclusive) {
            endInclusive = value
        }
    }

    fun updateEndInclusive(value: LocalDate?) {
        endInclusive = value
        val currentStart = start
        if (value != null && currentStart != null && value < currentStart) {
            start = value
        }
    }

    fun clear() {
        start = null
        endInclusive = null
    }

    companion object {
        fun saver(): Saver<RoutineDateRangeCardState, Any> = listSaver(
            save = {
                listOf(
                    it.start?.toEpochDays(),
                    it.endInclusive?.toEpochDays(),
                )
            },
            restore = {
                RoutineDateRangeCardState(
                    initialStart = it[0]?.let(LocalDate::fromEpochDays),
                    initialEndInclusive = it[1]?.let(LocalDate::fromEpochDays),
                )
            },
        )
    }
}

@Composable
internal fun rememberRoutineDateRangeCardState(
    vararg inputs: Any?,
    initialStart: LocalDate? = null,
    initialEndInclusive: LocalDate? = null,
): RoutineDateRangeCardState {
    return rememberSaveable(
        inputs = inputs,
        saver = RoutineDateRangeCardState.saver(),
    ) {
        RoutineDateRangeCardState(
            initialStart = initialStart,
            initialEndInclusive = initialEndInclusive,
        )
    }
}
