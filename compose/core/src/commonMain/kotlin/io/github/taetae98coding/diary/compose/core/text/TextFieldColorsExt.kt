package io.github.taetae98coding.diary.compose.core.text

import androidx.compose.material3.TextFieldColors
import androidx.compose.ui.graphics.Color

public fun TextFieldColors.transparentIndicator(): TextFieldColors {
    return copy(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
    )
}
