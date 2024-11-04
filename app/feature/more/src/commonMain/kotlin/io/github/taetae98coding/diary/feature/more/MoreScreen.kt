package io.github.taetae98coding.diary.feature.more

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.core.resources.Res
import io.github.taetae98coding.diary.core.resources.more
import io.github.taetae98coding.diary.feature.more.account.MoreAccount
import io.github.taetae98coding.diary.feature.more.account.state.MoreAccountUiState
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MoreScreen(
    accountUiStateProvider: () -> MoreAccountUiState,
    onLogin: () -> Unit,
    onJoin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text(text = stringResource(Res.string.more)) })
        },
    ) {
        Content(
            accountUiStateProvider = accountUiStateProvider,
            onLogin = onLogin,
            onJoin = onJoin,
            modifier = Modifier.fillMaxSize()
                .padding(it)
                .padding(DiaryTheme.dimen.screenPaddingValues),
        )
    }
}

@Composable
private fun Content(
    accountUiStateProvider: () -> MoreAccountUiState,
    onLogin: () -> Unit,
    onJoin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
            .then(modifier),
    ) {
        MoreAccount(
            uiStateProvider = accountUiStateProvider,
            onLogin = onLogin,
            onJoin = onJoin,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
