package io.github.taetae98coding.diary.feature.buddy.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.core.model.buddy.BuddyGroup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BuddyListScreen(
    listProvider: () -> List<BuddyGroup>?,
    onGroup: (String) -> Unit,
    modifier: Modifier = Modifier,
    floatingActionButton: @Composable () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(text = "버디") }) },
        floatingActionButton = floatingActionButton,
    ) {
        Content(
            listProvider = listProvider,
            onGroup = onGroup,
            modifier = Modifier.fillMaxSize()
                .padding(it),
        )
    }
}

@Composable
private fun Content(
    listProvider: () -> List<BuddyGroup>?,
    onGroup: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = DiaryTheme.dimen.screenPaddingValues,
        verticalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace),
    ) {
        val list = listProvider()

        if (list == null) {
            items(
                count = 5,
                contentType = { "BuddyGroup" },
            ) {
                BuddyListItem(
                    group = null,
                    onClick = {},
                    modifier = Modifier.animateItem(),
                )
            }
        } else if (list.isEmpty()) {
            item(
                key = "Loading",
                contentType = "Loading",
            ) {
                Box(
                    modifier = Modifier.fillParentMaxSize()
                        .animateItem(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "그룹이 없어요 🐭",
                        style = DiaryTheme.typography.headlineMedium,
                    )
                }
            }
        } else {
            items(
                items = list,
                key = { it.id },
                contentType = { "BuddyGroup" },
            ) {
                BuddyListItem(
                    group = it,
                    onClick = { onGroup(it.id) },
                    modifier = Modifier.animateItem(),
                )
            }
        }
    }
}

@Composable
private fun BuddyListItem(
    group: BuddyGroup?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = group?.detail?.title.orEmpty(),
                style = DiaryTheme.typography.titleLarge,
            )
        }
    }
}
