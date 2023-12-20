package com.taetae98.diary.feature.memo.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.taetae98.diary.ui.compose.button.AddFloatingButton
import com.taetae98.diary.ui.compose.entity.EntityDetail
import com.taetae98.diary.ui.compose.entity.EntityDetailUiState
import com.taetae98.diary.ui.compose.topbar.NavigateUpTopBar

@Composable
internal fun MemoDetailScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    uiState: State<MemoDetailUiState>,
    detailUiState: State<EntityDetailUiState>
) {
    Scaffold(
        modifier = modifier,
        topBar = { NavigateUpTopBar(onNavigateUp = onNavigateUp) },
        floatingActionButton = {
            FloatingButton(uiState = uiState)
        }
    ) {
        Content(
            modifier = Modifier.padding(it),
            detailUiState = detailUiState,
        )
    }
}

@Composable
private fun FloatingButton(
    modifier: Modifier = Modifier,
    uiState: State<MemoDetailUiState>,
) {
    when (val value = uiState.value) {
        is MemoDetailUiState.Add -> {
            AddFloatingButton(
                modifier = modifier,
                onAdd = value.onAdd
            )
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    detailUiState: State<EntityDetailUiState>
) {
    Column(
        modifier = modifier.verticalScroll(state = rememberScrollState())
    ) {
        EntityDetail(uiState = detailUiState)
    }
}
