package io.github.taetae98coding.diary.compose.core.color

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.compose.core.preview.ComponentPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme

@Composable
public fun ColorCircle(
    colorProvider: () -> Color,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.background(color = colorProvider(), shape = CircleShape))
}

@ComponentPreview
@Composable
private fun Preview() {
    DiaryTheme {
        ColorCircle(
            colorProvider = { Color.Red },
            modifier = Modifier.size(40.dp),
        )
    }
}
