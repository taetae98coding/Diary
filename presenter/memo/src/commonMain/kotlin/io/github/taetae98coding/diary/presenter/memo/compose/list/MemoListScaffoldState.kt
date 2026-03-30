package io.github.taetae98coding.diary.presenter.memo.compose.list

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.retain.retain

public class MemoListScaffoldState {
    public val hostState: SnackbarHostState = SnackbarHostState()
}

@Composable
public fun rememberMemoListScaffoldState(): MemoListScaffoldState {
    return retain { MemoListScaffoldState() }
}
