package io.github.taetae98coding.diary.compose.core.box

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
public fun LoadingBox(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        ContainedLoadingIndicator()
    }
}
