package com.taetae98.diary.ui.compose.switch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
public fun TextSwitch(
    modifier: Modifier = Modifier,
    text: String,
    uiState: State<SwitchUiState>,
) {
    TextSwitch(
        modifier = modifier,
        text = text,
        value = uiState.value.value,
        onValueChange = uiState.value.onValueChange,
    )
}

@Composable
public fun TextSwitch(
    modifier: Modifier = Modifier,
    text: String,
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier.toggleable(
            value = value,
            role = Role.Switch,
            onValueChange = onValueChange,
        ).padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = text)
        Switch(
            checked = value,
            onCheckedChange = null,
        )
    }
}
