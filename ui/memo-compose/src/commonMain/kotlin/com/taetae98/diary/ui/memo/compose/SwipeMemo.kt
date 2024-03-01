@file:OptIn(ExperimentalMaterial3Api::class)

package com.taetae98.diary.ui.memo.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.taetae98.diary.ui.compose.icon.CircleIcon
import com.taetae98.diary.ui.compose.icon.DeleteIcon
import com.taetae98.diary.ui.compose.icon.FinishIcon

@Composable
public fun SwipeMemo(
    modifier: Modifier = Modifier,
    uiState: SwipeMemoUiState?,
) {
    val state = rememberSwipeToDismissBoxState(
        initialValue = SwipeToDismissBoxValue.Settled,
        confirmValueChange = {
            if (uiState == null) return@rememberSwipeToDismissBoxState false

            when (it) {
                SwipeToDismissBoxValue.StartToEnd -> uiState.onFinish()
                SwipeToDismissBoxValue.EndToStart -> uiState.onDelete()
                else -> Unit
            }

            true
        },
    )

    SwipeToDismissBox(
        modifier = Modifier.clip(MemoDefaults.shape)
            .then(modifier),
        state = state,
        backgroundContent = {
            Box(
                modifier = Modifier.fillMaxSize()
                    .drawBehind {
                        val backgroundColor = when (state.dismissDirection) {
                            SwipeToDismissBoxValue.EndToStart -> Color.Red
                            SwipeToDismissBoxValue.StartToEnd -> Color.Green
                            else -> null
                        }?.copy(alpha = 0.5F)

                        backgroundColor?.let(::drawRect)
                    }
                    .padding(horizontal = 8.dp),
            ) {
                when (state.dismissDirection) {
                    SwipeToDismissBoxValue.EndToStart -> SwipeDeleteButton(state = state)
                    SwipeToDismissBoxValue.StartToEnd -> SwipeFinishButton(state = state)
                    else -> Unit
                }
            }
        },
        content = {
            Memo(
                modifier = Modifier.fillMaxWidth(),
                uiState = uiState?.memo,
            )
        }
    )
}

@Composable
private fun BoxScope.SwipeDeleteButton(
    modifier: Modifier = Modifier,
    state: SwipeToDismissBoxState,
) {
    SwipeIcon(
        modifier = modifier,
        state = state,
        dismissValue = SwipeToDismissBoxValue.EndToStart,
    ) {
        DeleteIcon()
    }
}

@Composable
private fun BoxScope.SwipeFinishButton(
    modifier: Modifier = Modifier,
    state: SwipeToDismissBoxState,
) {
    SwipeIcon(
        modifier = modifier,
        state = state,
        dismissValue = SwipeToDismissBoxValue.StartToEnd,
    ) {
        FinishIcon()
    }
}

@Composable
private fun BoxScope.SwipeIcon(
    modifier: Modifier = Modifier,
    state: SwipeToDismissBoxState,
    dismissValue: SwipeToDismissBoxValue,
    content: @Composable () -> Unit,
) {
    val alignment = when (dismissValue) {
        SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
        SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
        else -> Alignment.Center
    }

    val scale by animateFloatAsState(
        when (state.targetValue) {
            dismissValue -> 1.33F
            else -> 0.66F
        },
    )

    when (state.targetValue) {
        dismissValue -> Box(
            modifier = modifier.align(alignment)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
        ) {
            content()
        }

        SwipeToDismissBoxValue.Settled -> CircleIcon(
            modifier = modifier.align(alignment)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
        )

        else -> Unit
    }
}
