package com.taetae98.diary.ui.entity

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.taetae98.diary.ui.compose.text.ClearTextField
import com.taetae98.diary.ui.compose.text.TextFieldUiState

@Composable
public fun EntityTitle(
    modifier: Modifier = Modifier,
    uiState: State<TextFieldUiState>,
) {
    Card(
        modifier = modifier.width(intrinsicSize = IntrinsicSize.Min)
    ) {
        ClearTextField(
            modifier = Modifier.fillMaxWidth(),
            uiState = uiState,
            label = {
                Text(text = "제목")
            },
            singleLine = true,
            maxLines = 1,
            minLines = 1,
        )
    }
}
