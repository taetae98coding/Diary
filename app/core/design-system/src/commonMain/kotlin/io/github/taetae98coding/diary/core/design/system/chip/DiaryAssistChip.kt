package io.github.taetae98coding.diary.core.design.system.chip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ChipColors
import androidx.compose.material3.ChipElevation
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

@Composable
public fun DiaryAssistChip(
	onClick: () -> Unit,
	label: @Composable () -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	leadingIcon: @Composable (() -> Unit)? = null,
	trailingIcon: @Composable (() -> Unit)? = null,
	shape: Shape = AssistChipDefaults.shape,
	colors: ChipColors = AssistChipDefaults.assistChipColors(),
	elevation: ChipElevation? = AssistChipDefaults.assistChipElevation(),
	border: BorderStroke? = AssistChipDefaults.assistChipBorder(enabled),
	interactionSource: MutableInteractionSource? = null,
) {
	CompositionLocalProvider(
		LocalMinimumInteractiveComponentSize provides Dp.Unspecified,
	) {
		AssistChip(
			onClick = onClick,
			label = label,
			modifier = modifier,
			leadingIcon = leadingIcon,
			trailingIcon = trailingIcon,
			shape = shape,
			colors = colors,
			elevation = elevation,
			border = border,
			interactionSource = interactionSource,
		)
	}
}
