package io.github.taetae98coding.diary.compose.core.color

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.compose.core.icon.RefreshIcon
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.library.compose.ui.random
import io.github.taetae98coding.diary.library.compose.ui.rgbText
import io.github.taetae98coding.diary.library.compose.ui.wcagAAAContentColor
import kotlinx.coroutines.launch

@Composable
public fun ColorPicker(
    state: ColorPickerState,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.animateContentSize()) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .drawBehind { drawRect(state.value) },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "#${state.targetValue.rgbText()}",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 40.dp),
                color = state.targetValue.wcagAAAContentColor(),
            )
            IconButton(
                onClick = { coroutineScope.launch { state.animateTo(Color.random()) } },
                modifier = Modifier.padding(4.dp)
                    .align(Alignment.TopEnd),
                colors = IconButtonDefaults.iconButtonColors(contentColor = state.targetValue.wcagAAAContentColor()),
            ) {
                RefreshIcon()
            }
        }
        RgbSlider(
            state = state.redState,
            label = "R",
            color = Color(0xFFFF4444),
        )
        RgbSlider(
            state = state.greenState,
            label = "G",
            color = Color(0xFF2BFD47),
        )
        RgbSlider(
            state = state.blueState,
            label = "B",
            color = Color(0xFF5D83FF),
        )
    }
}

@Composable
private fun RgbSlider(
    state: SliderState,
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            color = color,
            style = DiaryTheme.typography.labelLarge,
        )
        Slider(
            state = state,
            modifier = Modifier.weight(1F),
        )
        SliderValueText(state = state)
    }
}

@Composable
private fun SliderValueText(
    state: SliderState,
    modifier: Modifier = Modifier,
) {
    Text(
        text = (state.value * 255).toInt().toString(),
        modifier = modifier,
        style = DiaryTheme.typography.bodySmall,
    )
}
