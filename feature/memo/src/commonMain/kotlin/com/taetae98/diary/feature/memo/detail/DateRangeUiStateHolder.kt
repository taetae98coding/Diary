package com.taetae98.diary.feature.memo.detail

import com.taetae98.diary.library.kotlin.ext.localDateNow
import com.taetae98.diary.library.kotlin.ext.randomRgbColor
import com.taetae98.diary.library.kotlin.ext.toEpochMilliseconds
import com.taetae98.diary.library.viewmodel.SavedStateHandle
import com.taetae98.diary.ui.entity.DateRangeUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

public class DateRangeUiStateHolder(
    scope: CoroutineScope,
    private val key: String = "DateRangeUiStateHolder",
    initialHasDate: Boolean = false,
    initialColor: Long = randomRgbColor(),
    initialStart: Long = localDateNow().toEpochMilliseconds(),
    initialEnd: Long = initialStart,
    private val savedStateHandle: SavedStateHandle,
) {
    private val hasDate = savedStateHandle.getStateFlow(
        key = "$key$HAS_DATE_KEY",
        initialValue = initialHasDate,
    )

    private val color = savedStateHandle.getStateFlow(
        key = "$key$COLOR_KEY",
        initialValue = initialColor,
    )

    private val start = savedStateHandle.getStateFlow(
        key = "$key$START_KEY",
        initialValue = initialStart,
    )

    private val endInclusive = savedStateHandle.getStateFlow(
        key = "$key$END_INCLUSIVE_KEY",
        initialValue = initialEnd,
    )

    public val uiState: StateFlow<DateRangeUiState> = combine(
        hasDate,
        color,
        start,
        endInclusive
    ) { hasDate, color, start, endInclusive ->
        DateRangeUiState(
            hasDate = hasDate,
            onHasDateChange = ::setHasDate,
            color = color,
            onColorChange = ::setColor,
            start = start,
            setStart = ::setStart,
            endInclusive = endInclusive,
            setEndInclusive = ::setEndInclusive,
        )
    }.stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = DateRangeUiState(
            hasDate = hasDate.value,
            onHasDateChange = ::setHasDate,
            color = color.value,
            onColorChange = ::setColor,
            start = start.value,
            setStart = ::setStart,
            endInclusive = endInclusive.value,
            setEndInclusive = ::setEndInclusive,
        )
    )

    public fun getValue(): DateRangeUiState {
        return uiState.value
    }

    public fun setHasDate(hasDate: Boolean) {
        savedStateHandle["$key$HAS_DATE_KEY"] = hasDate
    }

    public fun setColor(color: Long) {
        savedStateHandle["$key$COLOR_KEY"] = color
    }

    public fun setStart(milliSeconds: Long) {
        savedStateHandle["$key$START_KEY"] = milliSeconds
        if (endInclusive.value < milliSeconds) {
            savedStateHandle["$key$END_INCLUSIVE_KEY"] = milliSeconds
        }
    }

    public fun setEndInclusive(milliSeconds: Long) {
        savedStateHandle["$key$END_INCLUSIVE_KEY"] = milliSeconds
        if (start.value > milliSeconds) {
            savedStateHandle["$key$START_KEY"] = milliSeconds
        }
    }

    public companion object {
        private const val HAS_DATE_KEY = "dateRangeUiStateHolderHasDateKey"
        private const val COLOR_KEY = "dateRangeUiStateHolderColorKey"
        private const val START_KEY = "dateRangeUiStateHolderStartKey"
        private const val END_INCLUSIVE_KEY = "dateRangeUiStateHolderEndInclusiveKey"
    }
}