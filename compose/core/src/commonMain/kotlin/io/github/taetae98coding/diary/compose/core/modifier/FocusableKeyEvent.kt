package io.github.taetae98coding.diary.compose.core.modifier

import androidx.compose.foundation.focusable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.onKeyEvent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect

public fun Modifier.focusableKeyEvent(
    autoFocus: Boolean = true,
    onKeyEvent: (KeyEvent) -> Boolean,
): Modifier = composed {
    val focusRequester = remember { FocusRequester() }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        if (autoFocus) {
            focusRequester.requestFocus()
        }
    }

    Modifier
        .focusRequester(focusRequester)
        .onKeyEvent(onKeyEvent)
        .focusable()
}
