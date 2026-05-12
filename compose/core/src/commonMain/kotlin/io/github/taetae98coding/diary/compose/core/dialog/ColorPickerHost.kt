@file:OptIn(ExperimentalMaterial3Api::class)

package io.github.taetae98coding.diary.compose.core.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.taetae98coding.diary.compose.core.color.ColorPicker
import io.github.taetae98coding.diary.compose.core.color.rememberColorPickerState

@Composable
public fun ColorPickerHost(
    state: DialogState,
    colorProvider: () -> Color,
    onSelect: (Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.isVisible) {
        val pickerState = rememberColorPickerState(initialColor = colorProvider())

        BasicAlertDialog(
            onDismissRequest = state::hide,
        ) {
            Card {
                ColorPicker(
                    state = pickerState,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = {
                            onSelect(pickerState.value)
                            state.hide()
                        },
                    ) {
                        Text(text = "확인")
                    }
                }
            }
        }
    }
}
