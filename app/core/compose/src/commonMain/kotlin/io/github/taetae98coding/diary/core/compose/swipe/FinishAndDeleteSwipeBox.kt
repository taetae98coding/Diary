package io.github.taetae98coding.diary.core.compose.swipe

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.design.system.icon.CircleIcon
import io.github.taetae98coding.diary.core.design.system.icon.DeleteIcon
import io.github.taetae98coding.diary.core.design.system.icon.FinishIcon
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme

@Composable
public fun FinishAndDeleteSwipeBox(
	modifier: Modifier = Modifier,
	onFinish: () -> Unit,
	onDelete: () -> Unit,
	content: @Composable () -> Unit,
) {
	val state = rememberSwipeToDismissBoxState(
		confirmValueChange = {
			when (it) {
				SwipeToDismissBoxValue.StartToEnd -> onFinish()
				SwipeToDismissBoxValue.EndToStart -> onDelete()
				else -> Unit
			}

			true
		},
	)

	SwipeToDismissBox(
		state = state,
		backgroundContent = {
			Box(
				modifier = Modifier
					.fillMaxSize(),
			) {
				val scale by animateFloatAsState(
					targetValue = if (state.targetValue == SwipeToDismissBoxValue.Settled) {
						0.5F
					} else {
						1.4F
					},
				)

				Box(
					modifier = Modifier
						.align(Alignment.CenterStart)
						.padding(horizontal = 4.dp)
						.graphicsLayer {
							scaleX = scale
							scaleY = scale
						},
				) {
					if (state.targetValue == SwipeToDismissBoxValue.Settled) {
						CircleIcon()
					} else if (state.targetValue == SwipeToDismissBoxValue.StartToEnd) {
						FinishIcon(tint = DiaryTheme.color.primary)
					}
				}

				Box(
					modifier = Modifier
						.align(Alignment.CenterEnd)
						.padding(horizontal = 4.dp)
						.graphicsLayer {
							scaleX = scale
							scaleY = scale
						},
				) {
					if (state.targetValue == SwipeToDismissBoxValue.Settled) {
						CircleIcon()
					} else if (state.targetValue == SwipeToDismissBoxValue.EndToStart) {
						DeleteIcon(tint = DiaryTheme.color.secondary)
					}
				}
			}
		},
		modifier = modifier,
		content = {
			content()
		},
	)
}
