package com.taetae98.diary.feature.tag.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.taetae98.diary.ui.compose.button.AddFloatingButton
import com.taetae98.diary.ui.compose.scaffold.DiaryScaffold
import com.taetae98.diary.ui.compose.switch.SwitchUiState
import com.taetae98.diary.ui.compose.switch.TextSwitch
import com.taetae98.diary.ui.compose.text.TextFieldUiState
import com.taetae98.diary.ui.compose.topbar.NavigateUpTopBar
import com.taetae98.diary.ui.entity.EntityDescription
import com.taetae98.diary.ui.entity.EntityTitle

@Composable
internal fun TagAddScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    onAdd: () -> Unit,
    titleUiState: State<TextFieldUiState>,
    descriptionUiState: State<TextFieldUiState>,
    memoVisibleUiState: State<SwitchUiState>,
    calendarVisibleUiState: State<SwitchUiState>,
) {
    DiaryScaffold(
        modifier = modifier,
        topBar = { NavigateUpTopBar(onNavigateUp = onNavigateUp) },
        floatingActionButton = { AddFloatingButton(onClick = onAdd) }
    ) {
        Content(
            modifier = Modifier.padding(it),
            titleUiState = titleUiState,
            descriptionUiState = descriptionUiState,
            memoVisibleUiState = memoVisibleUiState,
            calendarVisibleUiState = calendarVisibleUiState,
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    titleUiState: State<TextFieldUiState>,
    descriptionUiState: State<TextFieldUiState>,
    memoVisibleUiState: State<SwitchUiState>,
    calendarVisibleUiState: State<SwitchUiState>,
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
        VisibleLayout(
            modifier = Modifier.fillMaxWidth(),
            memoVisibleUiState = memoVisibleUiState,
            calendarVisibleUiState = calendarVisibleUiState,
        )
    }
}

@Composable
private fun VisibleLayout(
    modifier: Modifier = Modifier,
    memoVisibleUiState: State<SwitchUiState>,
    calendarVisibleUiState: State<SwitchUiState>,
) {
    Card(
        modifier = modifier,
    ) {
        Column {
            TextSwitch(
                modifier = Modifier.fillMaxWidth(),
                text = "메모",
                uiState = memoVisibleUiState,
            )
            TextSwitch(
                modifier = Modifier.fillMaxWidth(),
                text = "캘린더",
                uiState = calendarVisibleUiState,
            )
        }
    }
}
