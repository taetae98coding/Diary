package io.github.taetae98coding.diary.core.design.system.color

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.design.system.icon.RefreshIcon
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.library.color.toContrastColor
import io.github.taetae98coding.diary.library.color.toRgbString

@Composable
public fun DiaryColorPicker(
    state: DiaryColorPickerState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        val sliderModifier = Modifier.padding(horizontal = DiaryTheme.dimen.diaryHorizontalPadding)

        ColorBox(
            state = state,
            modifier = Modifier.fillMaxWidth()
                .height(200.dp),
        )

        Spacer(modifier = Modifier.height(DiaryTheme.dimen.diaryVerticalPadding))

        ColorSlider(
            color = Color.Red,
            valueProvider = { state.red },
            onValueChange = state::onRedChange,
            modifier = sliderModifier,
        )

        ColorSlider(
            color = Color.Green,
            valueProvider = { state.green },
            onValueChange = state::onGreenChange,
            modifier = sliderModifier,
        )

        ColorSlider(
            color = Color.Blue,
            valueProvider = { state.blue },
            onValueChange = state::onBlueChange,
            modifier = sliderModifier,
        )
    }
}

@Composable
private fun ColorBox(
    state: DiaryColorPickerState,
    modifier: Modifier = Modifier,
) {
    val animateColor by animateColorAsState(state.color)

    Box(
        modifier = modifier.drawBehind {
            drawRect(color = animateColor)
        },
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "#${state.color.toArgb().toRgbString()}",
            color = state.color.toContrastColor(),
        )

        IconButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = state::refresh,
            colors = IconButtonDefaults.iconButtonColors(contentColor = state.color.toContrastColor()),
        ) {
            RefreshIcon()
        }
    }
}

@Composable
private fun ColorSlider(
    color: Color,
    valueProvider: () -> Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val animateValue by animateFloatAsState(valueProvider())

    Slider(
        modifier = modifier,
        value = animateValue,
        onValueChange = onValueChange,
        colors = SliderDefaults.colors(
            thumbColor = color,
            activeTrackColor = color,
        ),
    )
}
