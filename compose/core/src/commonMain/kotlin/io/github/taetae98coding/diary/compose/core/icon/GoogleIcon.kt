package io.github.taetae98coding.diary.compose.core.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.compose.core.icon.google.GoogleDark
import io.github.taetae98coding.diary.compose.core.icon.google.GoogleLight
import io.github.taetae98coding.diary.compose.core.preview.IconPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme

@Composable
public fun GoogleIcon(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
) {
    Image(
        imageVector = if (isDarkTheme) {
            Icons.Rounded.GoogleDark
        } else {
            Icons.Rounded.GoogleLight
        },
        contentDescription = contentDescription,
        modifier = modifier,
    )
}

@IconPreview
@Composable
private fun DarkPreview() {
    DiaryTheme {
        GoogleIcon(
            isDarkTheme = true,
        )
    }
}

@IconPreview
@Composable
private fun LightPreview() {
    DiaryTheme {
        GoogleIcon(
            isDarkTheme = false,
        )
    }
}
