package io.github.taetae98coding.diary.compose.core.card

import androidx.compose.animation.Animatable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import io.github.taetae98coding.diary.compose.core.dialog.DialogState
import io.github.taetae98coding.diary.compose.core.dialog.rememberDialogState
import io.github.taetae98coding.diary.library.compose.ui.random

@Stable
public class ColorCardState(
    initialColor: Color,
    public val dialogState: DialogState,
) {
    private val animatable = Animatable(initialColor)
    public val value: Color
        get() = animatable.value
    public val targetValue: Color
        get() = animatable.targetValue

    public suspend fun updateColor(color: Color) {
        animatable.animateTo(color)
    }

    public companion object {
        public fun saver(dialogState: DialogState): Saver<ColorCardState, Any> {
            return listSaver(
                save = { listOf(it.value.toArgb()) },
                restore = {
                    ColorCardState(
                        initialColor = Color(it[0]),
                        dialogState = dialogState,
                    )
                },
            )
        }
    }
}

@Composable
public fun rememberColorCardState(
    vararg inputs: Any?,
    initialColor: Color = remember { Color.random() },
): ColorCardState {
    val dialogState = rememberDialogState()

    return rememberSaveable(
        inputs = inputs,
        saver = ColorCardState.saver(dialogState),
    ) {
        ColorCardState(
            initialColor = initialColor,
            dialogState = dialogState,
        )
    }
}
