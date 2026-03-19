package io.github.taetae98coding.diary.compose.core.card

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import io.github.taetae98coding.diary.compose.core.preview.ComponentPreview
import io.github.taetae98coding.diary.compose.core.text.ClearTextField
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme

@Composable
public fun TitleCard(
    modifier: Modifier = Modifier,
    state: TitleCardState = rememberTitleCardState(),
) {
    Card(modifier = modifier) {
        ClearTextField(
            state = state.textFieldState,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(state.focusRequester),
            label = { Text(text = "제목") },
            lineLimits = TextFieldLineLimits.SingleLine,
        )
    }
}

@ComponentPreview
@Composable
private fun NotEmptyPreview() {
    DiaryTheme {
        TitleCard(state = rememberTitleCardState(initialText = "taetae98coding@diary.com"))
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    DiaryTheme {
        TitleCard()
    }
}
