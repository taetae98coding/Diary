package com.taetae98.diary.ui.compose.text

import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.taetae98.diary.ui.compose.icon.ClearIcon

@Composable
public fun ClearTextField(
    modifier: Modifier = Modifier,
    uiState: State<TextFieldUiState>,
    label: @Composable (() -> Unit)? = null,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
) {
    TextField(
        modifier = modifier,
        value = uiState.value.value,
        onValueChange = uiState.value.onValueChange,
        label = label,
        trailingIcon = {
            if (uiState.value.value.isNotEmpty()) {
                ClearButton(uiState = uiState)
            }
        },
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        colors = DiaryTextField.transparentIndicatorColor(),
    )
}

@Composable
private fun ClearButton(
    modifier: Modifier = Modifier,
    uiState: State<TextFieldUiState>,
) {
    IconButton(
        modifier = modifier,
        onClick = { uiState.value.onValueChange("") },
    ) {
        ClearIcon()
    }
}
