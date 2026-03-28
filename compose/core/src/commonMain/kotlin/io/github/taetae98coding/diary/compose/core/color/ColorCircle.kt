package io.github.taetae98coding.diary.compose.core.color

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
public fun ColorCircle(
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.background(color = color, shape = CircleShape))
}
