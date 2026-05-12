package io.github.taetae98coding.diary.feature.routine.home

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.retain.retain

@Stable
internal class RoutineHomeScaffoldState {
    val hostState: SnackbarHostState = SnackbarHostState()
}

@Composable
internal fun rememberRoutineHomeScaffoldState(): RoutineHomeScaffoldState {
    return retain { RoutineHomeScaffoldState() }
}
