package io.github.taetae98coding.diary.library.navigation3.runtime

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack

@Composable
public actual fun rememberNavBackStackCompat(vararg keys: NavKey): NavBackStack<NavKey> {
    return rememberNavBackStack(*keys)
}
