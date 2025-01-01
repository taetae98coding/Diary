package io.github.taetae98coding.diary.feature.account.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme

@Composable
internal fun BottomBarButton(
    onClick: () -> Unit,
    enableProvider: () -> Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val targetColor = if (enableProvider()) {
        DiaryTheme.color.primary
    } else {
        DiaryTheme.color.onSurface.copy(alpha = 0.12F)
    }
    val animatedButtonColor by animateColorAsState(targetValue = targetColor)

    Column(
        modifier = modifier
            .drawBehind { drawRect(animatedButtonColor) }
            .clickable(
                enabled = enableProvider(),
                role = Role.Button,
                onClick = onClick,
            ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(50.dp),
            contentAlignment = Alignment.Center,
        ) {
            CompositionLocalProvider(
                LocalContentColor provides contentColorFor(targetColor),
            ) {
                content()
            }
        }
        Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
    }
}
