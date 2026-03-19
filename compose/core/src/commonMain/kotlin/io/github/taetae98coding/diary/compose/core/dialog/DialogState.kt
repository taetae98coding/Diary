package io.github.taetae98coding.diary.compose.core.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue

@Stable
public class DialogState {
    public var isVisible: Boolean by mutableStateOf(false)
        private set

    public fun show() {
        isVisible = true
    }

    public fun hide() {
        isVisible = false
    }
}

@Composable
public fun rememberDialogState(): DialogState {
    return retain { DialogState() }
}
