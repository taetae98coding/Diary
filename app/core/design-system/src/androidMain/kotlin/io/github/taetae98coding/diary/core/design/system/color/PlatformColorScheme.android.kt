package io.github.taetae98coding.diary.core.design.system.color

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
internal actual fun platformDarkColorScheme(): ColorScheme = dynamicDarkColorScheme(LocalContext.current)

@Composable
internal actual fun platformLightColorScheme(): ColorScheme = dynamicLightColorScheme(LocalContext.current)
