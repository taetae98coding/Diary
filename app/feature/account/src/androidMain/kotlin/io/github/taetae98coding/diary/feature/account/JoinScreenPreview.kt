package io.github.taetae98coding.diary.feature.account

import androidx.compose.runtime.Composable
import io.github.taetae98coding.diary.core.design.system.preview.DiaryPreview
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.feature.account.join.JoinScreen
import io.github.taetae98coding.diary.feature.account.join.state.JoinUiState
import io.github.taetae98coding.diary.feature.account.join.state.rememberJoinScreenState

@Composable
@DiaryPreview
private fun JoinScreenPreview() {
    DiaryTheme {
        JoinScreen(
            state = rememberJoinScreenState(),
            onNavigateUp = {},
            onJoin = {},
            uiStateProvider = { JoinUiState() },
            onLoginFinish = {}
        )
    }
}
