package io.github.taetae98coding.diary.compose.core.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.compose.core.dialog.ColorPickerHost
import io.github.taetae98coding.diary.compose.core.preview.ComponentPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.library.compose.ui.rgbText
import io.github.taetae98coding.diary.library.compose.ui.wcagAAAContentColor
import kotlinx.coroutines.launch

@Composable
public fun ColorCard(
    modifier: Modifier = Modifier,
    state: ColorCardState = rememberColorCardState(),
) {
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier.height(80.dp)
            .clip(CardDefaults.shape)
            .drawBehind { drawRect(color = state.value) }
            .clickable(onClick = state.dialogState::show),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val red = (state.targetValue.red * 255).toInt()
            val green = (state.targetValue.green * 255).toInt()
            val blue = (state.targetValue.blue * 255).toInt()

            Text(
                text = "#${state.targetValue.rgbText()}",
                color = state.targetValue.wcagAAAContentColor(),
                style = DiaryTheme.typography.titleMedium,
            )
            Text(
                text = "RGB($red, $green, $blue)",
                color = state.targetValue.wcagAAAContentColor(),
                textAlign = TextAlign.Center,
                style = DiaryTheme.typography.bodySmall,
            )
        }
    }

    ColorPickerHost(
        state = state.dialogState,
        colorProvider = { state.value },
        onSelect = { color -> coroutineScope.launch { state.updateColor(color) } },
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    DiaryTheme {
        ColorCard()
    }
}
