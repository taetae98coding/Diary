package io.github.taetae98coding.diary.compose.core.text

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldLabelScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.testTag
import io.github.taetae98coding.diary.compose.core.icon.ClearIcon

public const val CLEAR_BUTTON_TEST_TAG: String = "ClearButton"

@Composable
public fun ClearTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    label: @Composable (TextFieldLabelScope.() -> Unit)? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
    colors: TextFieldColors = TextFieldDefaults
        .colors()
        .transparentIndicator(),
) {
    val focusRequester = remember { FocusRequester() }
    val isNotBlank by remember { derivedStateOf { state.text.isNotBlank() } }

    TextField(
        state = state,
        modifier = modifier.focusRequester(focusRequester),
        label = label,
        trailingIcon = {
            AnimatedVisibility(
                visible = isNotBlank,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
            ) {
                IconButton(
                    onClick = {
                        state.clearText()
                        focusRequester.requestFocus()
                    },
                    modifier = Modifier.testTag(CLEAR_BUTTON_TEST_TAG),
                ) {
                    ClearIcon()
                }
            }
        },
        lineLimits = lineLimits,
        colors = colors,
    )
}
