package io.github.taetae98coding.diary.compose.core.button

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.compose.core.icon.CheckIcon
import io.github.taetae98coding.diary.compose.core.preview.ComponentPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme

@Composable
public fun CheckFloatingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isInProgressProvider: () -> Boolean = { false },
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Crossfade(
            targetState = isInProgressProvider(),
        ) { isInProgress ->
            if (isInProgress) {
                CircularWavyProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                CheckIcon()
            }
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    DiaryTheme {
        CheckFloatingButton(onClick = {})
    }
}

@ComponentPreview
@Composable
private fun InProgressPreview() {
    DiaryTheme {
        CheckFloatingButton(onClick = {}, isInProgressProvider = { true })
    }
}
