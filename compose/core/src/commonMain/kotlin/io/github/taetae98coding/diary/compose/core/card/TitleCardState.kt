package io.github.taetae98coding.diary.compose.core.card

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.focus.FocusRequester

@Stable
public class TitleCardState(public val textFieldState: TextFieldState) {
    public val focusRequester: FocusRequester = FocusRequester()
}

@Composable
public fun rememberTitleCardState(
    vararg inputs: Any?,
    initialText: String = "",
): TitleCardState {
    val textFieldState = rememberSaveable(
        inputs = inputs,
        saver = TextFieldState.Saver,
    ) {
        TextFieldState(initialText = initialText)
    }

    return retain(textFieldState) {
        TitleCardState(
            textFieldState = textFieldState,
        )
    }
}
