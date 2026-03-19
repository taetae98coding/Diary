package io.github.taetae98coding.diary.compose.core.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

public data class DiaryDimen(
    val screenHorizontalPadding: Dp,
    val screenVerticalPAdding: Dp,
    val screenCardSpace: Dp,
) {
    val screenPaddingValues: PaddingValues
        get() = PaddingValues(horizontal = screenHorizontalPadding, vertical = screenVerticalPAdding)

    public companion object {
        internal val COMPAT = DiaryDimen(
            screenHorizontalPadding = 20.dp,
            screenVerticalPAdding = 16.dp,
            screenCardSpace = 16.dp,
        )

        internal val LARGE = COMPAT.copy(
            screenHorizontalPadding = 24.dp,
        )
    }
}

internal val LocalDiaryDimen: ProvidableCompositionLocal<DiaryDimen> = staticCompositionLocalOf { error("No LocalDiaryDimen") }
