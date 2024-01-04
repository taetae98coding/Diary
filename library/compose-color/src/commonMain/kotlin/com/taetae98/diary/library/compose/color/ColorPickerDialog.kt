package com.taetae98.diary.library.compose.color

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun ColorPickerDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Card {
            Content()
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(6.dp),
    ) {
        val red = rememberSaveable { mutableStateOf(0F) }
        val green = rememberSaveable { mutableStateOf(0F) }
        val blue = rememberSaveable { mutableStateOf(0F) }

        TextLayout(
            modifier = Modifier.fillMaxWidth()
                .heightIn(150.dp),
            red = red,
            green = green,
            blue = blue,
        )
        ColorSlider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Red,
            state = red,
        )
        ColorSlider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Green,
            state = green
        )
        ColorSlider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Blue,
            state = blue
        )
    }
}

@Composable
private fun TextLayout(
    modifier: Modifier = Modifier,
    red: MutableState<Float>,
    green: MutableState<Float>,
    blue: MutableState<Float>,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        TextField(
            value = "${red.value}${green.value}${blue.value}",
            onValueChange = {},
        )
    }
}

@Composable
private fun ColorSlider(
    modifier: Modifier = Modifier,
    color: Color,
    state: MutableState<Float>,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Slider(
            modifier = Modifier.weight(1F),
            value = state.value,
            onValueChange = { state.value = it },
            valueRange = 0F..255F,
            steps = 254,
            colors = SliderDefaults.colors(
                thumbColor = color,
                activeTrackColor = color,
                activeTickColor = color,
            )
        )
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "0000",
                color = Color.Transparent,
                style = MaterialTheme.typography.labelMedium,
            )
            Text(
                text = state.value.roundToInt().toString(),
                color = color,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}