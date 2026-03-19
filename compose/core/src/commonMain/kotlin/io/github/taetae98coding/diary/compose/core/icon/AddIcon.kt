package io.github.taetae98coding.diary.compose.core.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.compose.core.preview.IconPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme

@Composable
public fun AddIcon(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Rounded.Add,
        contentDescription = null,
        modifier = modifier,
    )
}

@IconPreview
@Composable
private fun Preview() {
    DiaryTheme {
        Surface {
            AddIcon()
        }
    }
}
