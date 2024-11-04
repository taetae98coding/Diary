package io.github.taetae98coding.diary.library.shimmer.m3

import androidx.compose.foundation.background
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.library.color.multiplyAlpha

@Composable
public fun Modifier.shimmer(): Modifier {
    return background(color = LocalContentColor.current.multiplyAlpha(0.38F))
}
