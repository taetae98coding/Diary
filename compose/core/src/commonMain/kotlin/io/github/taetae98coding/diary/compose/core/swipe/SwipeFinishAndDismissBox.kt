package io.github.taetae98coding.diary.compose.core.swipe

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.compose.core.icon.CircleIcon
import io.github.taetae98coding.diary.compose.core.icon.DeleteIcon
import io.github.taetae98coding.diary.compose.core.icon.FinishIcon
import kotlinx.coroutines.flow.collectLatest

@Composable
public fun SwipeFinishAndDismissBox(
    onFinish: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val positionalThreshold = SwipeToDismissBoxDefaults.positionalThreshold
    val state = remember { SwipeToDismissBoxState(SwipeToDismissBoxValue.Settled, positionalThreshold) }

    SwipeToDismissBox(
        state = state,
        backgroundContent = {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (state.currentValue != SwipeToDismissBoxValue.EndToStart) {
                    AnimateIconBox(
                        state = state,
                        targetValue = SwipeToDismissBoxValue.StartToEnd,
                        modifier = Modifier.width(56.dp)
                            .fillMaxHeight(),
                    ) { iconSize ->
                        FinishIcon(modifier = Modifier.size(iconSize))
                    }
                }

                Spacer(modifier = Modifier.weight(1F))

                if (state.currentValue != SwipeToDismissBoxValue.StartToEnd) {
                    AnimateIconBox(
                        state = state,
                        targetValue = SwipeToDismissBoxValue.EndToStart,
                        modifier = Modifier.width(56.dp)
                            .fillMaxHeight(),
                    ) { iconSize ->
                        DeleteIcon(modifier = Modifier.size(iconSize))
                    }
                }
            }
        },
        modifier = modifier,
        onDismiss = { value ->
            when (value) {
                SwipeToDismissBoxValue.StartToEnd -> onFinish()
                SwipeToDismissBoxValue.EndToStart -> onDelete()
                else -> Unit
            }
        },
    ) {
        content()
    }

    LaunchedEffect(state, hapticFeedback) {
        snapshotFlow { state.targetValue }
            .collectLatest {
                if (it != SwipeToDismissBoxValue.Settled) {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate)
                }
            }
    }
}

@Composable
private fun AnimateIconBox(
    state: SwipeToDismissBoxState,
    targetValue: SwipeToDismissBoxValue,
    modifier: Modifier = Modifier,
    icon: @Composable (Dp) -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        val isTargetState = state.currentValue == targetValue
        val animateSize by animateDpAsState(
            targetValue = if (isTargetState) {
                32.dp
            } else {
                16.dp
            },
        )

        Crossfade(
            targetState = isTargetState,
            modifier = Modifier.size(animateSize),
        ) { isTargetState ->
            if (isTargetState) {
                icon(animateSize)
            } else {
                CircleIcon(modifier = Modifier.size(animateSize))
            }
        }
    }
}
