package com.taetae98.diary.feature.tag.add

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.taetae98.diary.ui.compose.button.AddFloatingButton
import com.taetae98.diary.ui.compose.scaffold.DiaryScaffold
import com.taetae98.diary.ui.compose.text.TextFieldUiState
import com.taetae98.diary.ui.compose.topbar.NavigateUpTopBar
import com.taetae98.diary.ui.entity.EntityTitle

@Composable
internal fun TagAddScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    titleUiState: State<TextFieldUiState>,
    onAdd: () -> Unit,
) {
    DiaryScaffold(
        modifier = modifier,
        topBar = { NavigateUpTopBar(onNavigateUp = onNavigateUp) },
        floatingActionButton = { AddFloatingButton(onClick = onAdd) }
    ) {
        Content(
            modifier = Modifier.padding(it),
            titleUiState = titleUiState,
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    titleUiState: State<TextFieldUiState>,
) {
    Column(
        modifier = modifier.verticalScroll(state = rememberScrollState()),
    ) {
        EntityTitle(
            modifier = Modifier.fillMaxWidth(),
            uiState = titleUiState,
        )
    }
}
