package com.taetae98.diary.feature.memo.detail

import com.taetae98.diary.library.viewmodel.SavedStateHandle
import com.taetae98.diary.ui.entity.DateRangeUiState
import kotlin.random.Random
import kotlin.random.nextLong
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

public class DateRangeUiStateHolder(
    scope: CoroutineScope,
    initialColor: Long,
    private val colorKey: String,
    private val savedStateHandle: SavedStateHandle,
) {
    private val color = savedStateHandle.getStateFlow(
        key = colorKey,
        initialValue = initialColor,
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    public val uiState: StateFlow<DateRangeUiState> = color.mapLatest {
        DateRangeUiState(
            color = it,
            onColorChange = ::setColor,
        )
    }.stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = DateRangeUiState(
            color = color.value,
            onColorChange = ::setColor,
        )
    )

    public fun setColor(color: Long) {
        savedStateHandle[colorKey] = color
    }
}