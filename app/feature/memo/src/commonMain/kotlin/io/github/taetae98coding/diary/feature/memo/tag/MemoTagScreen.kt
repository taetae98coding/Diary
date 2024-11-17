package io.github.taetae98coding.diary.feature.memo.tag

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.core.design.system.icon.NavigateUpIcon
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MemoTagScreen(
    navigateButtonProvider: () -> MemoTagNavigationButton,
    onTagAdd: () -> Unit,
    memoTagListProvider: () -> List<TagUiState>?,
    tagListProvider: () -> List<TagUiState>?,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "메모 태그") },
                navigationIcon = {
                    when (val button = navigateButtonProvider()) {
                        is MemoTagNavigationButton.NavigateUp -> {
                            IconButton(onClick = button.onNavigateUp) {
                                NavigateUpIcon()
                            }
                        }

                        is MemoTagNavigationButton.None -> Unit
                    }
                },
            )
        },
    ) {
        Content(
            onTagAdd = onTagAdd,
            memoTagListProvider = memoTagListProvider,
            tagListProvider = tagListProvider,
            modifier = Modifier.fillMaxWidth()
                .padding(it)
                .padding(DiaryTheme.dimen.diaryPaddingValues),
        )
    }
}

@Composable
private fun Content(
    onTagAdd: () -> Unit,
    memoTagListProvider: () -> List<TagUiState>?,
    tagListProvider: () -> List<TagUiState>?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace),
    ) {
        TagFlow(
            modifier = Modifier.fillMaxWidth()
                .weight(3F),
            listProvider = memoTagListProvider,
            title = {
                Text(
                    text = "캘린더 태그",
                    modifier = Modifier.fillMaxWidth()
                        .minimumInteractiveComponentSize()
                        .padding(horizontal = DiaryTheme.dimen.diaryHorizontalPadding),
                )
            },
            empty = { Text(text = "태그가 없어요 🐻") },
            tag = { PrimaryMemoTag(uiState = it) },
        )

        TagFlow(
            modifier = Modifier.fillMaxWidth()
                .weight(7F),
            listProvider = tagListProvider,
            title = {
                Text(
                    text = "태그",
                    modifier = Modifier.fillMaxWidth()
                        .minimumInteractiveComponentSize()
                        .padding(horizontal = DiaryTheme.dimen.diaryHorizontalPadding),
                )
            },
            empty = {
                Button(onClick = onTagAdd) {
                    Text(text = "태그 추가하러 가기")
                }
            },
            tag = { MemoTag(uiState = it) },
        )
    }
}
