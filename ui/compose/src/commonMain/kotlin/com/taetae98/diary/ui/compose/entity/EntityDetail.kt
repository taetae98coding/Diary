package com.taetae98.diary.ui.compose.entity

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier

@Composable
public fun EntityDetail(
    modifier: Modifier = Modifier,
    uiState: State<EntityDetailUiState>,
) {
    EntityDetail(
        modifier = modifier,
        uiState = uiState.value
    )
}

@Composable
public fun EntityDetail(
    modifier: Modifier = Modifier,
    uiState: EntityDetailUiState,
) {
    Card(
        modifier = modifier
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.title,
            onValueChange = uiState.setTitle,
        )
    }
}