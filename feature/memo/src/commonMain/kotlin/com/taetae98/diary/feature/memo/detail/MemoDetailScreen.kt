package com.taetae98.diary.feature.memo.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.LazyPagingItems
import com.taetae98.diary.feature.memo.tag.TagUiState
import com.taetae98.diary.ui.compose.button.AddFloatingButton
import com.taetae98.diary.ui.compose.button.CheckFloatingButton
import com.taetae98.diary.ui.compose.icon.AddIcon
import com.taetae98.diary.ui.compose.icon.DeleteIcon
import com.taetae98.diary.ui.compose.icon.FinishIcon
import com.taetae98.diary.ui.compose.icon.TagIcon
import com.taetae98.diary.ui.compose.scaffold.DiaryScaffold
import com.taetae98.diary.ui.compose.text.TextFieldUiState
import com.taetae98.diary.ui.compose.topbar.NavigateUpTopBar
import com.taetae98.diary.ui.entity.DateRangeUiState
import com.taetae98.diary.ui.entity.EntityDateRange
import com.taetae98.diary.ui.entity.EntityDescription
import com.taetae98.diary.ui.entity.EntityTitle

@Composable
internal fun MemoDetailScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    uiState: State<MemoDetailUiState>,
    toolbarUiState: State<MemoDetailToolbarUiState>,
    titleUiState: State<TextFieldUiState>,
    descriptionUiState: State<TextFieldUiState>,
    dateRangeUiState: State<DateRangeUiState>,
    tagUiState: LazyPagingItems<TagUiState>
) {
    val hostState = remember { SnackbarHostState() }

    DiaryScaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                onNavigateUp = onNavigateUp,
                toolbarUiState = toolbarUiState,
            )
        },
        floatingActionButton = {
            FloatingButton(uiState = uiState)
        },
        snackbarHost = {
            SnackbarHost(hostState = hostState)
        },
    ) {
        Content(
            modifier = Modifier.padding(it),
            titleUiState = titleUiState,
            descriptionUiState = descriptionUiState,
            dateRangeUiState = dateRangeUiState,
            tagUiState = tagUiState,
        )
    }

    Message(
        uiState = uiState,
        hostState = hostState,
    )
}

@Composable
internal fun TopBar(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    toolbarUiState: State<MemoDetailToolbarUiState>,
) {
    NavigateUpTopBar(
        modifier = modifier,
        onNavigateUp = onNavigateUp,
        actions = {
            when (val value = toolbarUiState.value) {
                is MemoDetailToolbarUiState.Detail -> {
                    IconButton(onClick = value.onComplete) {
                        val tint = if (value.isComplete) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            LocalContentColor.current
                        }

                        FinishIcon(tint = tint)
                    }
                    IconButton(onClick = value.onDelete) {
                        DeleteIcon()
                    }
                }

                else -> Unit
            }
        }
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

            MemoDetailMessage.TitleEmpty -> {
                hostState.showSnackbar("제목을 입력해주세요.")
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
                onClick = value.onAdd
            )
        }

        is MemoDetailUiState.Detail -> {
            CheckFloatingButton(
                modifier = modifier,
                onClick = value.onUpdate
            )
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    titleUiState: State<TextFieldUiState>,
    descriptionUiState: State<TextFieldUiState>,
    dateRangeUiState: State<DateRangeUiState>,
    tagUiState: LazyPagingItems<TagUiState>
) {
    Column(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        EntityTitle(
            modifier = Modifier.fillMaxWidth(),
            uiState = titleUiState
        )
        EntityDescription(
            modifier = Modifier.fillMaxWidth(),
            uiState = descriptionUiState,
        )
        EntityDateRange(
            modifier = Modifier.fillMaxWidth(),
            uiState = dateRangeUiState,
        )
        TagLayout(tagUiState = tagUiState)
    }
}

@Composable
private fun TagLayout(
    modifier: Modifier = Modifier,
    tagUiState: LazyPagingItems<TagUiState>,
) {
    Card(
        modifier = modifier
    ) {
        Column {
            val isVisible = remember { mutableStateOf(false) }

            TagLayoutTitle(
                modifier = Modifier.fillMaxWidth(),
                isVisible = isVisible,
            )

            AnimatedVisibility(
                modifier = Modifier.fillMaxWidth()
                    .heightIn(max = 300.dp),
                visible = isVisible.value,
            ) {
                TagAllLayout(
                    tagUiState = tagUiState,
                )
            }
        }
    }
}

@Composable
private fun TagLayoutTitle(
    modifier: Modifier = Modifier,
    isVisible: MutableState<Boolean>
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(12.dp),
            text = "태그"
        )

        IconButton(onClick = { isVisible.value = !isVisible.value }) {
            if (isVisible.value) {
                TagIcon(tint = MaterialTheme.colorScheme.primary)
            } else {
                AddIcon()
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun TagAllLayout(
    modifier: Modifier = Modifier,
    tagUiState: LazyPagingItems<TagUiState>,
) {
    FlowRow(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
    ) {
        repeat(tagUiState.itemCount) {
            val item = tagUiState[it]

            key(item?.id ?: it) {
                FilterChip(
                    selected = item?.isSelected ?: false,
                    onClick = { item?.onClick() },
                    label = { Text(text = item?.title.orEmpty()) },
                    leadingIcon = { TagIcon() },
                    shape = CircleShape,
                )
            }
        }
    }
}
