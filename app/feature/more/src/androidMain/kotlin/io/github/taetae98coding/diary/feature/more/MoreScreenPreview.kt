package io.github.taetae98coding.diary.feature.more

import androidx.compose.runtime.Composable
import io.github.taetae98coding.diary.core.design.system.preview.DiaryPreview
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.feature.more.account.state.MoreAccountUiState

@Composable
@DiaryPreview
private fun MoreScreenPreview() {
    DiaryTheme {
        MoreScreen(
            accountUiStateProvider = { MoreAccountUiState.Loading },
            onLogin = {},
            onJoin = {},
        )
    }
}
