package io.github.taetae98coding.diary.feature.account.join

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.feature.account.join.state.rememberJoinScreenState
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun JoinRoute(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    joinViewModel: JoinViewModel = koinViewModel(),
) {
    val state = rememberJoinScreenState()
    val uiState by joinViewModel.uiState.collectAsStateWithLifecycle()

    JoinScreen(
        state = state,
        onNavigateUp = navigateUp,
        onJoin = { joinViewModel.join(state.email, state.password) },
        uiStateProvider = { uiState },
        onLoginFinish = navigateUp,
        modifier = modifier,
    )
}
