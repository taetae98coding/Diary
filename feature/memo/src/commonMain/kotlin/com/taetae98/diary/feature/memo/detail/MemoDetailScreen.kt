package com.taetae98.diary.feature.memo.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.taetae98.diary.ui.compose.button.AddFloatingButton
import com.taetae98.diary.ui.compose.entity.EntityDetail
import com.taetae98.diary.ui.compose.text.TextFieldUiState
import com.taetae98.diary.ui.compose.topbar.NavigateUpTopBar

@Composable
internal fun MemoDetailScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    uiState: State<MemoDetailUiState>,
    titleUiState: State<TextFieldUiState>,
) {
    val hostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        topBar = { NavigateUpTopBar(onNavigateUp = onNavigateUp) },
        floatingActionButton = {
            FloatingButton(uiState = uiState)
        },
        snackbarHost = {
            SnackbarHost(hostState = hostState)
        }
    ) {
        Content(
            modifier = Modifier.padding(it),
            titleUiState = titleUiState,
        )
    }

    Message(
        uiState = uiState,
        hostState = hostState,
    )
}

@Composable
private fun Message(
    uiState: State<MemoDetailUiState>,
    hostState: SnackbarHostState,
) {
    LaunchedEffect(uiState.value) {
        when (uiState.value.message) {
            MemoDetailMessage.Add -> {
                hostState.showSnackbar("메모 추가")
                uiState.value.onMessageShown()
            }

            else -> Unit
        }
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

        else -> Unit
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    titleUiState: State<TextFieldUiState>,
) {
    Column(
        modifier = modifier.verticalScroll(state = rememberScrollState())
    ) {
        EntityDetail(
            modifier = Modifier.fillMaxWidth(),
            titleUiState = titleUiState
        )
    }
}
