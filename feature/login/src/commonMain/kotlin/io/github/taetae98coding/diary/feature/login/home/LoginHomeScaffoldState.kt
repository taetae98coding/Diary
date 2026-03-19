package io.github.taetae98coding.diary.feature.login.home

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.retain.retain

@Stable
internal class LoginHomeScaffoldState {
    val hostState: SnackbarHostState = SnackbarHostState()
}

@Composable
internal fun rememberLoginHomeScaffoldState(): LoginHomeScaffoldState {
    return retain { LoginHomeScaffoldState() }
}
