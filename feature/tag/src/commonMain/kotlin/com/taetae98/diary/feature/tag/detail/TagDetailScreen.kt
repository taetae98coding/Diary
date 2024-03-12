package com.taetae98.diary.feature.tag.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.taetae98.diary.library.compose.backhandler.KBackHandler
import com.taetae98.diary.ui.compose.icon.DeleteIcon
import com.taetae98.diary.ui.compose.scaffold.DiaryScaffold
import com.taetae98.diary.ui.compose.text.TextFieldUiState
import com.taetae98.diary.ui.compose.topbar.NavigateUpTopBar
import com.taetae98.diary.ui.entity.EntityDescription
import com.taetae98.diary.ui.entity.EntityTitle

@Composable
internal fun TagDetailScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    uiState: TagDetailUiState,
    message: State<TagDetailMessage?>,
    toolbarUiState: TagDetailToolbarUiState,
    toolbarMessage: State<TagDetailToolbarMessage?>,
    titleUiState: State<TextFieldUiState>,
    descriptionUiState: State<TextFieldUiState>,
) {
    val hostState = remember { SnackbarHostState() }

    DiaryScaffold(
        modifier = modifier,
        topBar = {
            NavigateUpTopBar(
                onNavigateUp = uiState.onUpsert,
                actions = {
                    IconButton(onClick = toolbarUiState.onDelete) {
                        DeleteIcon()
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = hostState) },
    ) {
        Content(
            modifier = Modifier.padding(it),
            titleUiState = titleUiState,
            descriptionUiState = descriptionUiState,
        )
    }

    KBackHandler(onBack = uiState.onUpsert)

    Message(
        onNavigateUp = onNavigateUp,
        state = message,
    )

    ToolbarMessage(
        onNavigateUp = onNavigateUp,
        state = toolbarMessage,
    )
}

@Composable
private fun Message(
    onNavigateUp: () -> Unit,
    state: State<TagDetailMessage?>,
) {
    LaunchedEffect(state.value) {
        when (val message = state.value) {
            is TagDetailMessage.Upsert, is TagDetailMessage.UpsertFail -> {
                onNavigateUp()
                message.messageShown()
            }

            else -> Unit
        }
    }
}

@Composable
private fun ToolbarMessage(
    onNavigateUp: () -> Unit,
    state: State<TagDetailToolbarMessage?>,
) {
    LaunchedEffect(state.value) {
        when (val message = state.value) {
            is TagDetailToolbarMessage.Delete -> {
                onNavigateUp()
                message.messageShown()
            }

            else -> Unit
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    titleUiState: State<TextFieldUiState>,
    descriptionUiState: State<TextFieldUiState>,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        EntityTitle(
            modifier = Modifier.fillMaxWidth(),
            uiState = titleUiState,
        )
        EntityDescription(
            modifier = Modifier.fillMaxWidth(),
            uiState = descriptionUiState,
        )
    }
}
