package com.taetae98.diary.ui.memo.compose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
public fun Memo(
    modifier: Modifier = Modifier,
    uiState: MemoUiState?,
    shape: Shape = MemoDefaults.shape,
) {
    Card(
        modifier = modifier,
        shape = shape,
    ) {
        val dateRange = remember {
            listOfNotNull(uiState?.dateRange?.start, uiState?.dateRange?.endInclusive)
                .distinct()
                .joinToString(separator = "~")
        }
        val verticalPadding = if (dateRange.isEmpty()) {
            10.dp
        } else {
            8.dp
        }

        Spacer(modifier = Modifier.height(verticalPadding))
        Text(
            modifier = Modifier.padding(horizontal = 12.dp),
            text = uiState?.title.orEmpty(),
            style = MaterialTheme.typography.titleMedium,
        )
        if (dateRange.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(horizontal = 12.dp),
                text = dateRange,
                style = MaterialTheme.typography.labelMedium,
            )
        }
        Spacer(modifier = Modifier.height(verticalPadding))
    }
}