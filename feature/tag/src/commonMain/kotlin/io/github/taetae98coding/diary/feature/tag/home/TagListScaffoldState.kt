package io.github.taetae98coding.diary.feature.tag.home

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.retain.retain

@Stable
internal class TagListScaffoldState {
    val hostState: SnackbarHostState = SnackbarHostState()
}

@Composable
internal fun rememberTagListScaffoldState(): TagListScaffoldState {
    return retain { TagListScaffoldState() }
}
