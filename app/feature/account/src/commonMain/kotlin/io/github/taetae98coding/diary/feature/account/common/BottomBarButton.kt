package io.github.taetae98coding.diary.feature.account.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme

@Composable
internal fun BottomBarButton(
    onClick: () -> Unit,
    enableProvider: () -> Boolean,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    val animatedButtonColor by animateColorAsState(
        targetValue = if (enableProvider()) {
            DiaryTheme.color.primary
        } else {
            DiaryTheme.color.onSurface.copy(alpha = 0.12F)
        },
    )

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enableProvider(),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = animatedButtonColor,
            disabledContainerColor = animatedButtonColor,
        ),
        content = content,
    )
}

@Composable
internal fun BottomBarButtonContent(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier.fillMaxWidth()
            .height(50.dp)
            .windowInsetsPadding(NavigationBarDefaults.windowInsets),
        contentAlignment = Alignment.Center,
        content = content,
    )
}
