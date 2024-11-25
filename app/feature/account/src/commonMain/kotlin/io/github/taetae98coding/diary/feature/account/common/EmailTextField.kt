package io.github.taetae98coding.diary.feature.account.common

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import io.github.taetae98coding.diary.core.design.system.icon.EmailIcon
import io.github.taetae98coding.diary.core.design.system.text.ClearTextField

@Composable
internal fun EmailTextField(
	valueProvider: () -> String,
	onValueChange: (String) -> Unit,
	modifier: Modifier = Modifier,
) {
	val focusRequester = remember { FocusRequester() }

	ClearTextField(
		valueProvider = valueProvider,
		onValueChange = onValueChange,
		modifier = modifier.focusRequester(focusRequester),
		placeholder = { Text(text = "이메일") },
		leadingIcon = { EmailIcon() },
		keyboardOptions = KeyboardOptions(
			capitalization = KeyboardCapitalization.None,
			keyboardType = KeyboardType.Email,
			imeAction = ImeAction.Next,
		),
		singleLine = true,
	)

	LaunchedEffect(focusRequester) {
		focusRequester.requestFocus()
	}
}
