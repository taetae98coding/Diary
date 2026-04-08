package io.github.taetae98coding.diary.compose.core.button

import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.compose.core.icon.NavigateUpIcon
import io.github.taetae98coding.diary.compose.core.preview.ComponentPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme

@Composable
public fun NavigateUpButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(onClick = onClick) {
        NavigateUpIcon()
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    DiaryTheme {
        Surface {
            NavigateUpButton(onClick = {})
        }
    }
}
