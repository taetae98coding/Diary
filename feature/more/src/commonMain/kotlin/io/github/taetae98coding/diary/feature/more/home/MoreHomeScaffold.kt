package io.github.taetae98coding.diary.feature.more.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import io.github.taetae98coding.diary.compose.core.preview.ScreenPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme

@Composable
internal fun MoreHomeScaffold(
    modifier: Modifier = Modifier,
    accountUiStateProvider: () -> MoreHomeAccountUiState = { MoreHomeAccountUiState.Loading },
    onLogin: () -> Unit = {},
    onLogout: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopBar() },
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 120.dp),
            modifier = Modifier.padding(paddingValues),
            contentPadding = DiaryTheme.dimen.screenPaddingValues,
        ) {
            item(
                span = { GridItemSpan(maxLineSpan) },
            ) {
                MoreHomeAccountCard(
                    uiStateProvider = accountUiStateProvider,
                    onLogin = dropUnlessResumed(block = onLogin),
                    onLogout = dropUnlessResumed(block = onLogout),
                )
            }
        }
    }
}

@Composable
private fun TopBar(modifier: Modifier = Modifier) {
    TopAppBar(
        title = { Text(text = "더보기") },
        modifier = modifier,
    )
}

@ScreenPreview
@Composable
private fun Preview() {
    DiaryTheme {
        MoreHomeScaffold()
    }
}
