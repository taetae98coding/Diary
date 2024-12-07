package io.github.taetae98coding.diary.core.design.system.text

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.VisualTransformation
import io.github.taetae98coding.diary.core.design.system.icon.ClearIcon

@Composable
@NonRestartableComposable
public fun ClearTextField(
	valueProvider: () -> String,
	onValueChange: (String) -> Unit,
	modifier: Modifier = Modifier,
	label: @Composable (() -> Unit)? = null,
	placeholder: @Composable (() -> Unit)? = null,
	leadingIcon: @Composable (() -> Unit)? = null,
	trailingIcon: @Composable (() -> Unit)? = null,
	errorProvider: () -> Boolean = { false },
	visualTransformation: VisualTransformation = VisualTransformation.None,
	keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
	keyboardActions: KeyboardActions = KeyboardActions.Default,
	singleLine: Boolean = false,
	shape: Shape = TextFieldDefaults.shape,
) {
	val focusRequester = remember { FocusRequester() }

	TextField(
		value = valueProvider(),
		onValueChange = onValueChange,
		modifier = modifier.focusRequester(focusRequester),
		label = label,
		placeholder = placeholder,
		leadingIcon = leadingIcon,
		trailingIcon = {
			Row {
				if (trailingIcon != null) {
					trailingIcon()
				}

				if (valueProvider().isNotEmpty()) {
					IconButton(
						onClick = {
							onValueChange("")
							focusRequester.requestFocus()
						},
					) {
						ClearIcon()
					}
				}
			}
		},
		isError = errorProvider(),
		visualTransformation = visualTransformation,
		keyboardOptions = keyboardOptions,
		keyboardActions = keyboardActions,
		singleLine = singleLine,
		shape = shape,
		colors = TextFieldDefaults.colors(
			focusedIndicatorColor = Color.Transparent,
			unfocusedIndicatorColor = Color.Transparent,
			disabledIndicatorColor = Color.Transparent,
			errorIndicatorColor = Color.Transparent,
		),
	)
}
