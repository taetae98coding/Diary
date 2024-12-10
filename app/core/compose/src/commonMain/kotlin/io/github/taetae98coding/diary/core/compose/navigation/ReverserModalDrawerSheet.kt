package io.github.taetae98coding.diary.core.compose.navigation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import io.github.taetae98coding.diary.core.design.system.shape.start
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme

@Composable
public fun ReverseModalDrawerSheet(
    modifier: Modifier = Modifier,
    drawerShape: Shape = DiaryTheme.shape.large.start(),
    drawerContainerColor: Color = DrawerDefaults.modalContainerColor,
    drawerContentColor: Color = contentColorFor(drawerContainerColor),
    drawerTonalElevation: Dp = DrawerDefaults.ModalDrawerElevation,
    windowInsets: WindowInsets = DrawerDefaults.windowInsets,
    content: @Composable ColumnScope.() -> Unit
) {
    ModalDrawerSheet(
        modifier = modifier,
        drawerShape = drawerShape,
        drawerContainerColor = drawerContainerColor,
        drawerContentColor = drawerContentColor,
        drawerTonalElevation = drawerTonalElevation,
        windowInsets = windowInsets,
        content = content
    )
}
