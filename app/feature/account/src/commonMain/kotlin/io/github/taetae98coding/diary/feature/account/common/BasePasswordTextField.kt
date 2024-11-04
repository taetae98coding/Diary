package io.github.taetae98coding.diary.feature.account.common

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import io.github.taetae98coding.diary.core.design.system.icon.VisibilityOffIcon
import io.github.taetae98coding.diary.core.design.system.icon.VisibilityOnIcon
import io.github.taetae98coding.diary.core.design.system.text.ClearTextField
import io.github.taetae98coding.diary.core.resources.icon.KeyIcon

@Composable
internal fun BasePasswordTextField(
    valueProvider: () -> String,
    onValueChange: (String) -> Unit,
    placeholder: @Composable () -> Unit,
    passwordVisibleProvider: () -> Boolean,
    onPasswordVisibleChange: (Boolean) -> Unit,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    ClearTextField(
        valueProvider = valueProvider,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        leadingIcon = { KeyIcon() },
        trailingIcon = {
            IconButton(onClick = { onPasswordVisibleChange(!passwordVisibleProvider()) }) {
                Crossfade(passwordVisibleProvider()) { isVisible ->
                    if (isVisible) {
                        VisibilityOnIcon()
                    } else {
                        VisibilityOffIcon()
                    }
                }
            }
        },
        visualTransformation = if (passwordVisibleProvider()) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
    )
}
