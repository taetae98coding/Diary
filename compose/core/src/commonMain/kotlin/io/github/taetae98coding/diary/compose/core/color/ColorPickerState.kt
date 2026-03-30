package io.github.taetae98coding.diary.compose.core.color

import androidx.compose.animation.Animatable
import androidx.compose.material3.SliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import io.github.taetae98coding.diary.library.compose.ui.random

public class ColorPickerState(initialColor: Color) {
    internal val redState = SliderState(value = initialColor.red)
    internal val greenState = SliderState(value = initialColor.green)
    internal val blueState = SliderState(value = initialColor.blue)

    public val value: Color
        get() = Color(redState.value, greenState.value, blueState.value)

    public suspend fun animateTo(color: Color) {
        Animatable(value).animateTo(color) {
            redState.value = value.red
            greenState.value = value.green
            blueState.value = value.blue
        }
    }
}

private fun colorPickerStateSaver(): Saver<ColorPickerState, List<Float>> = Saver(
    save = { state ->
        listOf(
            state.value.red,
            state.value.green,
            state.value.blue,
        )
    },
    restore = { values ->
        ColorPickerState(
            initialColor = Color(values[0], values[1], values[2]),
        )
    },
)

@Composable
public fun rememberColorPickerState(initialColor: Color = remember { Color.random() }): ColorPickerState {
    return rememberSaveable(saver = colorPickerStateSaver()) {
        ColorPickerState(initialColor = initialColor)
    }
}
