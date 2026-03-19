package io.github.taetae98coding.diary.feature.more.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun MoreHomeScreen(
    navigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    accountViewModel: MoreHomeAccountViewModel = koinViewModel(),
) {
    val accountUiState by accountViewModel.uiState.collectAsStateWithLifecycle()

    MoreHomeScaffold(
        modifier = modifier,
        accountUiStateProvider = { accountUiState },
        onLogin = navigateToLogin,
        onLogout = accountViewModel::logout,
    )
}
