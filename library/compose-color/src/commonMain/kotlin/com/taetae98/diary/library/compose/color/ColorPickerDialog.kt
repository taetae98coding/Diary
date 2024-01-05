package com.taetae98.diary.library.compose.color

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import kotlin.math.roundToInt
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun ColorPickerDialog(
    modifier: Modifier = Modifier,
    color: Color?,
    onSelect: (Color) -> Unit,
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        properties = properties,
    ) {
        Card {
            Content(
                color = color,
                onSelect = onSelect,
                onDismissRequest = onDismissRequest,
            )
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    color: Color?,
    onSelect: (Color) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Column(modifier = modifier) {
        val red = rememberSaveable { mutableStateOf(color?.red ?: 0F) }
        val green = rememberSaveable { mutableStateOf(color?.green ?: 0F) }
        val blue = rememberSaveable { mutableStateOf(color?.blue ?: 0F) }

        Header(
            modifier = Modifier.fillMaxWidth()
                .heightIn(150.dp),
            red = red,
            green = green,
            blue = blue,
        )

        ColorSlider(
            modifier = Modifier.fillMaxWidth(),
            color = LocalRedDividerColor.current,
            state = red,
        )
        ColorSlider(
            modifier = Modifier.fillMaxWidth(),
            color = LocalGreenDividerColor.current,
            state = green
        )
        ColorSlider(
            modifier = Modifier.fillMaxWidth(),
            color = LocalBlueDividerColor.current,
            state = blue
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            TextButton(onClick = onDismissRequest) {
                Text(text = "취소")
            }
            TextButton(
                onClick = {
                    onSelect(Color(red.value, green.value, blue.value))
                }
            ) {
                Text(text = "선택")
            }
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
private fun Header(
    modifier: Modifier = Modifier,
    red: MutableState<Float>,
    green: MutableState<Float>,
    blue: MutableState<Float>,
) {
    val color by rememberUpdatedState(Color(red.value, green.value, blue.value))
    val contrastColor by rememberUpdatedState(color.toContrastColor())

    Box(
        modifier = modifier.background(color = color),
    ) {
        IconButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = {
                val randomColor = Color(
                    red = Random.nextInt(256),
                    green = Random.nextInt(256),
                    blue = Random.nextInt(256),
                )

                red.value = randomColor.red
                green.value = randomColor.green
                blue.value = randomColor.blue
            },
        ) {
            Icon(
                tint = contrastColor,
                imageVector = Icons.Rounded.Refresh,
                contentDescription = null,
            )
        }

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "#${color.value.toHexString(HexFormat.UpperCase).substring(2..7)}",
            color = contrastColor,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun ColorSlider(
    modifier: Modifier = Modifier,
    color: Color,
    state: MutableState<Float>,
) {
    Slider(
        modifier = modifier,
        value = state.value,
        onValueChange = { state.value = it },
        colors = SliderDefaults.colors(
            thumbColor = color,
            activeTrackColor = color,
            inactiveTrackColor = color,
        )
    )
}