package com.taetae98.diary.ui.compose.switch

import com.taetae98.diary.library.viewmodel.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

public class SwitchUiStateHolder(
    scope: CoroutineScope,
    private val key: String = "SwitchUiStateHolder",
    initialValue: Boolean,
    private val savedStateHandle: SavedStateHandle,
) {
    private val value = savedStateHandle.getStateFlow(
        key = key,
        initialValue = initialValue,
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    public val uiState: StateFlow<SwitchUiState> = value.mapLatest {
        SwitchUiState(
            value = it,
            onValueChange = ::setValue,
        )
    }.stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = SwitchUiState(
            value = value.value,
            onValueChange = ::setValue,
        )
    )

    public fun getValue(): SwitchUiState {
        return uiState.value
    }

    public fun setValue(value: Boolean) {
        savedStateHandle[key] = value
    }
}