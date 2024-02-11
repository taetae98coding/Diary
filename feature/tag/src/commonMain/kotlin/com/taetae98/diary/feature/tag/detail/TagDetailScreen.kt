package com.taetae98.diary.feature.tag.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.taetae98.diary.library.compose.backhandler.KBackHandler
import com.taetae98.diary.ui.compose.scaffold.DiaryScaffold
import com.taetae98.diary.ui.compose.text.TextFieldUiState
import com.taetae98.diary.ui.compose.topbar.NavigateUpTopBar
import com.taetae98.diary.ui.entity.EntityDescription
import com.taetae98.diary.ui.entity.EntityTitle

@Composable
internal fun TagDetailScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    titleUiState: State<TextFieldUiState>,
    descriptionUiState: State<TextFieldUiState>,
) {
    val hostState = remember { SnackbarHostState() }

    DiaryScaffold(
        modifier = modifier,
        topBar = { NavigateUpTopBar(onNavigateUp = onNavigateUp) },
        snackbarHost = { SnackbarHost(hostState = hostState) },
    ) {
        Content(
            modifier = Modifier.padding(it),
            titleUiState = titleUiState,
            descriptionUiState = descriptionUiState,
        )
    }

    KBackHandler(onBack = onNavigateUp)
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
