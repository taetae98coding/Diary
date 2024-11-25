package io.github.taetae98coding.diary.core.design.system.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import io.github.taetae98coding.diary.core.design.system.color.DiaryColor
import io.github.taetae98coding.diary.core.design.system.color.platformDarkColorScheme
import io.github.taetae98coding.diary.core.design.system.color.platformLightColorScheme
import io.github.taetae98coding.diary.core.design.system.dimen.DiaryDimen
import io.github.taetae98coding.diary.core.design.system.typography.DiaryTypography

public data object DiaryTheme {
	val color: DiaryColor
		@Composable
		get() = LocalDiaryColor.current

	val typography: DiaryTypography
		@Composable
		get() = LocalDiaryTypography.current

	val dimen: DiaryDimen
		@Composable
		get() = LocalDiaryDimen.current
}

@Composable
public fun DiaryTheme(
	content: @Composable () -> Unit,
) {
	val colorScheme = if (isSystemInDarkTheme()) {
		platformDarkColorScheme()
	} else {
		platformLightColorScheme()
	}

	CompositionLocalProvider(
		LocalDiaryColor provides DiaryColor(
			primary = colorScheme.primary,
			secondary = colorScheme.secondary,
			onPrimary = colorScheme.onPrimary,
			background = colorScheme.background,
			onSurface = colorScheme.onSurface,
		),
		LocalDiaryTypography provides DiaryTypography(
			headlineMedium = MaterialTheme.typography.headlineMedium,
			titleLarge = MaterialTheme.typography.titleLarge,
			titleMedium = MaterialTheme.typography.titleMedium,
			labelLarge = MaterialTheme.typography.labelLarge,
			labelMedium = MaterialTheme.typography.labelMedium,
			labelSmall = MaterialTheme.typography.labelSmall,
			bodySmall = MaterialTheme.typography.bodySmall,
		),
		LocalDiaryDimen provides DiaryDimen(),
	) {
		MaterialTheme(
			colorScheme = colorScheme,
			content = content,
		)
	}
}
