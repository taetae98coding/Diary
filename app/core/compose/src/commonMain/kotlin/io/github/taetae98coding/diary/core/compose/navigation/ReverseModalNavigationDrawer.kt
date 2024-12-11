package io.github.taetae98coding.diary.core.compose.navigation

import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

@Composable
public fun ReverseModalNavigationDrawer(
	drawerContent: @Composable () -> Unit,
	modifier: Modifier = Modifier,
	drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
	gesturesEnabled: Boolean = true,
	scrimColor: Color = DrawerDefaults.scrimColor,
	content: @Composable () -> Unit,
) {
	val originLayoutDirection = LocalLayoutDirection.current
	val reverseLayoutDirection = when (originLayoutDirection) {
		LayoutDirection.Ltr -> LayoutDirection.Rtl
		LayoutDirection.Rtl -> LayoutDirection.Ltr
	}

	CompositionLocalProvider(
		LocalLayoutDirection provides reverseLayoutDirection,
	) {
		ModalNavigationDrawer(
			drawerContent = {
				CompositionLocalProvider(
					LocalLayoutDirection provides originLayoutDirection,
				) {
					drawerContent()
				}
			},
			modifier = modifier,
			drawerState = drawerState,
			gesturesEnabled = gesturesEnabled,
			scrimColor = scrimColor,
			content = {
				CompositionLocalProvider(
					LocalLayoutDirection provides originLayoutDirection,
				) {
					content()
				}
			},
		)
	}
}
