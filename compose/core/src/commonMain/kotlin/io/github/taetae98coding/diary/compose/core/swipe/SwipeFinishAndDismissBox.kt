package io.github.taetae98coding.diary.compose.core.swipe

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
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
                AnimatedContent(
                    targetState = state.targetValue == SwipeToDismissBoxValue.StartToEnd,
                    transitionSpec = {
                        (fadeIn() + scaleIn()) togetherWith (fadeOut() + scaleOut())
                    },
                    contentAlignment = Alignment.Center,
                ) { isActive ->
                    if (isActive) {
                        FinishIcon()
                    } else {
                        CircleIcon()
                    }
                }

                Spacer(modifier = Modifier.weight(1F))

                AnimatedContent(
                    targetState = state.targetValue == SwipeToDismissBoxValue.EndToStart,
                    transitionSpec = {
                        (fadeIn() + scaleIn()) togetherWith (fadeOut() + scaleOut())
                    },
                    contentAlignment = Alignment.Center,
                ) { isActive ->
                    if (isActive) {
                        DeleteIcon()
                    } else {
                        CircleIcon()
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
