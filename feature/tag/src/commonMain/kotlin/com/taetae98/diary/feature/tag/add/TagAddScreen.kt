package com.taetae98.diary.feature.tag.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.taetae98.diary.feature.tag.detail.TagDetailMessage
import com.taetae98.diary.feature.tag.detail.TagDetailUiState
import com.taetae98.diary.ui.compose.button.AddFloatingButton
import com.taetae98.diary.ui.compose.scaffold.DiaryScaffold
import com.taetae98.diary.ui.compose.text.TextFieldUiState
import com.taetae98.diary.ui.compose.topbar.NavigateUpTopBar
import com.taetae98.diary.ui.entity.EntityDescription
import com.taetae98.diary.ui.entity.EntityTitle

@Composable
internal fun TagAddScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    uiState: State<TagDetailUiState>,
    titleUiState: State<TextFieldUiState>,
    descriptionUiState: State<TextFieldUiState>,
) {
    val hostState = remember { SnackbarHostState() }

    DiaryScaffold(
        modifier = modifier,
        topBar = { NavigateUpTopBar(onNavigateUp = onNavigateUp) },
        floatingActionButton = { FloatingButton(uiState = uiState) },
        snackbarHost = { SnackbarHost(hostState = hostState) },
    ) {
        Content(
            modifier = Modifier.padding(it),
            titleUiState = titleUiState,
            descriptionUiState = descriptionUiState,
        )
    }

    Message(
        uiState = uiState,
        hostState = hostState,
    )
}

@Composable
private fun FloatingButton(
    modifier: Modifier = Modifier,
    uiState: State<TagDetailUiState>,
) {
    when (val state = uiState.value) {
        is TagDetailUiState.Add -> {
            AddFloatingButton(
                modifier = modifier,
                onClick = state.onAdd
            )
        }
    }
}

@Composable
private fun Message(
    uiState: State<TagDetailUiState>,
    hostState: SnackbarHostState,
) {
    LaunchedEffect(uiState.value.message) {
        when (uiState.value.message) {
            TagDetailMessage.Add -> {
                hostState.showSnackbar("태그 추가")
                uiState.value.onMessageShown()
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
        verticalArrangement = Arrangement.spacedBy(8.dp)
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
