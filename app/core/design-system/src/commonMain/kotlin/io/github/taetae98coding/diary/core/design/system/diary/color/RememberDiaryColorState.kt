package io.github.taetae98coding.diary.core.design.system.diary.color

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import io.github.taetae98coding.diary.library.color.randomArgb

@Composable
public fun rememberDiaryColorState(
    vararg inputs: Any?,
    initialColor: Color = Color.Unspecified,
): DiaryColorState {
    return rememberSaveable(
        inputs = inputs,
        saver = DiaryColorState.saver(),
    ) {
        DiaryColorState(initialColor = initialColor.takeOrElse { Color(randomArgb()) })
    }
}
