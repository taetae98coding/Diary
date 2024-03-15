package com.taetae98.diary.ui.compose.text

import com.taetae98.diary.library.viewmodel.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

public class TextFieldUiStateHolder(
    scope: CoroutineScope,
    private val key: String = "TextFieldUiStateHolder",
    initialValue: String,
    private val savedStateHandle: SavedStateHandle,
    private val onValueChange: () -> Unit = {},
) {
    private val value = savedStateHandle.getStateFlow(
        key = key,
        initialValue = initialValue,
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    public val uiState: StateFlow<TextFieldUiState> = value.mapLatest {
        TextFieldUiState(
            value = it,
            onValueChange = ::setValue,
        )
    }.stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = TextFieldUiState(
            value = value.value,
            onValueChange = ::setValue,
        ),
    )

    public fun getValue(): TextFieldUiState {
        return uiState.value
    }

    public fun setValue(value: String) {
        savedStateHandle[key] = value
        onValueChange()
    }
}
