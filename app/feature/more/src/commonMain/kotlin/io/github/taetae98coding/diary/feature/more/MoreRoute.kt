package io.github.taetae98coding.diary.feature.more

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.feature.more.viewmodel.MoreAccountViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun MoreRoute(
    navigateToLogin: () -> Unit,
    navigateToJoin: () -> Unit,
    modifier: Modifier = Modifier,
    accountViewModel: MoreAccountViewModel = koinViewModel(),
) {
    val accountUiState by accountViewModel.uiState.collectAsStateWithLifecycle()

    MoreScreen(
        accountUiStateProvider = { accountUiState },
        onLogin = navigateToLogin,
        onJoin = navigateToJoin,
        modifier = modifier,
    )
}
