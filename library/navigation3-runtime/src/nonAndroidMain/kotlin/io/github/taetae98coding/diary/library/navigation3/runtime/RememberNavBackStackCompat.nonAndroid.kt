package io.github.taetae98coding.diary.library.navigation3.runtime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.retain.retain
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

@Composable
public actual fun rememberNavBackStackCompat(vararg keys: NavKey): NavBackStack<NavKey> {
    return retain { NavBackStack(*keys) }
}
