package io.github.taetae98coding.diary.core.design.system.color

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
internal actual fun platformDarkColorScheme(): ColorScheme = darkColorScheme()

@Composable
internal actual fun platformLightColorScheme(): ColorScheme = lightColorScheme()
