package io.github.taetae98coding.diary.feature.routine.home

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.retain.retain

internal class RoutineListScaffoldState {
    val hostState: SnackbarHostState = SnackbarHostState()
}

@Composable
internal fun rememberRoutineListScaffoldState(): RoutineListScaffoldState {
    return retain { RoutineListScaffoldState() }
}
