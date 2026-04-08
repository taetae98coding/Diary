package io.github.taetae98coding.diary.feature.login.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.compose.core.button.NavigateUpButton
import io.github.taetae98coding.diary.compose.core.icon.GoogleIcon
import io.github.taetae98coding.diary.compose.core.preview.ScreenPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme

@Composable
internal fun LoginHomeScaffold(
    modifier: Modifier = Modifier,
    state: LoginHomeScaffoldState = rememberLoginHomeScaffoldState(),
    isInProgressProvider: () -> Boolean = { false },
    onNavigateUp: () -> Unit = {},
    onGoogleLogin: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopBar(onNavigateUp = onNavigateUp) },
        snackbarHost = { SnackbarHost(hostState = state.hostState) },
    ) { paddingValues ->
        Crossfade(
            targetState = isInProgressProvider(),
            modifier = Modifier.padding(paddingValues)
                .fillMaxSize(),
        ) { isInProgress ->
            if (isInProgress) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    ContainedLoadingIndicator()
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    IconButton(onClick = onGoogleLogin) {
                        GoogleIcon()
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {},
) {
    TopAppBar(
        title = { Text(text = "로그인") },
        modifier = modifier,
        navigationIcon = { NavigateUpButton(onClick = onNavigateUp) },
    )
}

@ScreenPreview
@Composable
private fun Preview() {
    DiaryTheme {
        LoginHomeScaffold()
    }
}

@ScreenPreview
@Composable
private fun InProgressPreview() {
    DiaryTheme {
        LoginHomeScaffold(
            isInProgressProvider = { true },
        )
    }
}
