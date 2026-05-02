package io.github.taetae98coding.diary.feature.more.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.dropUnlessResumed
import io.github.taetae98coding.diary.compose.core.preview.ScreenPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.logger.analytics.api.AnalyticsLogEntry
import io.github.taetae98coding.diary.logger.core.DiaryLogger
import io.github.taetae98coding.diary.logger.crashlytics.api.CrashlyticsLogEntry

@Composable
internal fun MoreHomeScaffold(
    modifier: Modifier = Modifier,
    accountUiStateProvider: () -> MoreHomeAccountUiState = { MoreHomeAccountUiState.Loading },
    onLogin: () -> Unit = {},
    onLogout: () -> Unit = {},
    onGoldenHoliday: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopBar() },
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 64.dp),
            modifier = Modifier.padding(paddingValues),
            contentPadding = DiaryTheme.dimen.screenPaddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
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

            item {
                Card(
                    onClick = onGoldenHoliday,
                    modifier = Modifier.aspectRatio(1F),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "📆",
                            fontSize = 24.sp,
                        )
                        Text(
                            text = "황금연휴",
                            style = DiaryTheme.typography.bodySmall,
                        )
                    }
                }
            }

            item {
                Card(
                    onClick = {
                        val throwable = IllegalStateException("test crash")

                        DiaryLogger.log(AnalyticsLogEntry(name = "test_crash"))
                        DiaryLogger.log(CrashlyticsLogEntry(message = "test crash", throwable = throwable))

                        throw throwable
                    },
                    modifier = Modifier.aspectRatio(1F),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "💥",
                            fontSize = 24.sp,
                        )
                        Text(
                            text = "Crash",
                            style = DiaryTheme.typography.bodySmall,
                        )
                    }
                }
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
