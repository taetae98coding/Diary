package io.github.taetae98coding.diary.compose.core.card

import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.saveable.rememberSaveable

@Stable
public class DescriptionCardState(
    public val textFieldState: TextFieldState,
    internal val pagerState: PagerState,
)

@Composable
public fun rememberDescriptionCardState(
    vararg inputs: Any?,
    initialText: String = "",
    initialPage: Int = if (initialText.isBlank()) {
        0
    } else {
        1
    },
): DescriptionCardState {
    val textFieldState = rememberSaveable(
        inputs = inputs,
        saver = TextFieldState.Saver,
    ) {
        TextFieldState(initialText = initialText)
    }
    val pagerState = rememberPagerState(initialPage = initialPage) { 2 }

    return retain(textFieldState, pagerState) {
        DescriptionCardState(
            textFieldState = textFieldState,
            pagerState = pagerState,
        )
    }
}
