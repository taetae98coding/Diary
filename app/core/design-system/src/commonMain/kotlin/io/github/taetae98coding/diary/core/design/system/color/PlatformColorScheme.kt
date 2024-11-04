package io.github.taetae98coding.diary.core.design.system.color

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
internal expect fun platformDarkColorScheme(): ColorScheme

@Composable
internal expect fun platformLightColorScheme(): ColorScheme
