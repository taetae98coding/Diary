package com.taetae98.diary.ui.memo.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
public fun Memo(
    modifier: Modifier = Modifier,
    uiState: MemoUiState?,
    shape: Shape = MemoDefaults.shape
) {
    Card(
        modifier = modifier,
        shape = shape,
    ) {
        Text(
            modifier = Modifier.padding(12.dp),
            text = uiState?.title.orEmpty()
        )
    }
}