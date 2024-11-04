package io.github.taetae98coding.diary.feature.more

import androidx.compose.runtime.Composable
import io.github.taetae98coding.diary.core.design.system.preview.DiaryPreview
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.feature.more.account.MoreAccount
import io.github.taetae98coding.diary.feature.more.account.state.MoreAccountUiState

@Composable
@DiaryPreview
private fun LoadingPreview() {
    DiaryTheme {
        MoreAccount(
            uiStateProvider = { MoreAccountUiState.Loading },
            onLogin = {},
            onJoin = {},
        )
    }
}

@Composable
@DiaryPreview
private fun GuestPreview() {
    DiaryTheme {
        MoreAccount(
            uiStateProvider = { MoreAccountUiState.Guest },
            onLogin = {},
            onJoin = {},
        )
    }
}

@Composable
@DiaryPreview
private fun MemberPreview() {
    DiaryTheme {
        MoreAccount(
            uiStateProvider = {
                MoreAccountUiState.Member(
                    email = "taetae98coding@gmail.com",
                    logout = {},
                )
            },
            onLogin = {},
            onJoin = {},
        )
    }
}
