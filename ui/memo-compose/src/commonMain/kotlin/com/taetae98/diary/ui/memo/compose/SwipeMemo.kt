package com.taetae98.diary.ui.memo.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun SwipeMemo(
    modifier: Modifier = Modifier,
    uiState: SwipeMemoUiState?,
) {
    val state = rememberDismissState(
        initialValue = DismissValue.Default,
        confirmValueChange = {
            if (uiState == null) return@rememberDismissState false

            when (it) {
                DismissValue.DismissedToStart -> uiState.onDelete()
                DismissValue.DismissedToEnd -> uiState.onFinish()
                else -> Unit
            }

            true
        }
    )

    SwipeToDismiss(
        modifier = Modifier.clip(MemoDefaults.shape)
            .then(modifier),
        state = state,
        background = {
            Box(
                modifier = Modifier.fillMaxSize()
                    .drawBehind {
                        val backgroundColor = when (state.dismissDirection) {
                            DismissDirection.EndToStart -> Color.Red
                            DismissDirection.StartToEnd -> Color.Green
                            else -> null
                        }?.copy(alpha = 0.5F)

                        backgroundColor?.let(::drawRect)
                    }
                    .padding(horizontal = 8.dp)
            ) {
                when (state.dismissDirection) {
                    DismissDirection.EndToStart -> SwipeDeleteButton(state = state)
                    DismissDirection.StartToEnd -> SwipeFinishButton(state = state)
                    else -> Unit
                }
            }
        },
        dismissContent = {
            Memo(
                modifier = Modifier.fillMaxWidth(),
                uiState = uiState?.memo
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BoxScope.SwipeDeleteButton(
    modifier: Modifier = Modifier,
    state: DismissState,
) {
    SwipeIcon(
        modifier = modifier,
        state = state,
        dismissValue = DismissValue.DismissedToStart,
    ) {
        DeleteIcon()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BoxScope.SwipeFinishButton(
    modifier: Modifier = Modifier,
    state: DismissState,
) {
    SwipeIcon(
        modifier = modifier,
        state = state,
        dismissValue = DismissValue.DismissedToEnd,
    ) {
        FinishIcon()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BoxScope.SwipeIcon(
    modifier: Modifier = Modifier,
    state: DismissState,
    dismissValue: DismissValue,
    content: @Composable () -> Unit,
) {
    val alignment = when (dismissValue) {
        DismissValue.DismissedToEnd -> Alignment.CenterStart
        DismissValue.DismissedToStart -> Alignment.CenterEnd
        else -> Alignment.Center
    }

    val scale by animateFloatAsState(
        when (state.targetValue) {
            dismissValue -> 1.33F
            else -> 0.66F
        }
    )

    when (state.targetValue) {
        dismissValue -> Box(
            modifier = modifier.align(alignment)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
        ) {
            content()
        }

        DismissValue.Default -> CircleIcon(
            modifier = modifier.align(alignment)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
        )

        else -> Unit
    }
}
