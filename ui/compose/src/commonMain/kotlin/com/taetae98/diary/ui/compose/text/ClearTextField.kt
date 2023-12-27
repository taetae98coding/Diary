package com.taetae98.diary.ui.compose.text

import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.taetae98.diary.ui.compose.icon.ClearIcon

@Composable
public fun ClearTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        trailingIcon = {
            if (value.isNotEmpty()) {
                ClearButton { onValueChange("") }
            }
        },
        colors = DiaryTextField.transparentIndicatorColor(),
    )
}

@Composable
private fun ClearButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        ClearIcon()
    }
}