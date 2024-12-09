package io.github.taetae98coding.diary.core.compose.memo.tag

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.core.compose.tag.PrimaryTagCardItem
import io.github.taetae98coding.diary.core.compose.tag.TagCardFlow
import io.github.taetae98coding.diary.core.compose.tag.TagCardItem
import io.github.taetae98coding.diary.core.compose.tag.TagCardItemUiState
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun MemoTagScaffold(
    onTagAdd: () -> Unit,
    navigationIcon: @Composable () -> Unit,
    primaryTagListProvider: () -> List<TagCardItemUiState>?,
    tagListProvider: () -> List<TagCardItemUiState>?,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "메모 태그") },
                navigationIcon = navigationIcon,
            )
        },
    ) {
        Content(
            onTagAdd = onTagAdd,
            primaryTagListProvider = primaryTagListProvider,
            tagListProvider = tagListProvider,
            modifier = Modifier.fillMaxSize()
                .padding(it)
                .padding(DiaryTheme.dimen.screenPaddingValues)
        )
    }
}

@Composable
private fun Content(
    onTagAdd: () -> Unit,
    primaryTagListProvider: () -> List<TagCardItemUiState>?,
    tagListProvider: () -> List<TagCardItemUiState>?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace),
    ) {
        TagCardFlow(
            modifier = Modifier
                .fillMaxWidth()
                .weight(3F),
            listProvider = primaryTagListProvider,
            title = {
                Text(
                    text = "캘린더 태그",
                    modifier = Modifier
                        .fillMaxWidth()
                        .minimumInteractiveComponentSize()
                        .padding(horizontal = DiaryTheme.dimen.diaryHorizontalPadding),
                )
            },
            empty = { Text(text = "태그가 없어요 🐻") },
            tag = {
                PrimaryTagCardItem(uiState = it)
            },
        )

        TagCardFlow(
            modifier = Modifier
                .fillMaxWidth()
                .weight(7F),
            listProvider = tagListProvider,
            title = {
                Text(
                    text = "태그",
                    modifier = Modifier
                        .fillMaxWidth()
                        .minimumInteractiveComponentSize()
                        .padding(horizontal = DiaryTheme.dimen.diaryHorizontalPadding),
                )
            },
            empty = {
                Button(onClick = onTagAdd) {
                    Text(text = "태그 추가하러 가기")
                }
            },
            tag = { TagCardItem(uiState = it) },
        )
    }
}
