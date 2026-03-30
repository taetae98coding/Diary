package io.github.taetae98coding.diary.presenter.tag.compose.list

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.retain.retain

public class TagListScaffoldState {
    public val hostState: SnackbarHostState = SnackbarHostState()
}

@Composable
public fun rememberTagListScaffoldState(): TagListScaffoldState {
    return retain { TagListScaffoldState() }
}
