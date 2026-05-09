@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package io.github.taetae98coding.diary.compose.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

public data object DiaryTheme {
    val colorScheme: ColorScheme
        @Composable
        get() = MaterialTheme.colorScheme

    val typography: Typography
        @Composable
        get() = MaterialTheme.typography

    val dimen: DiaryDimen
        @Composable
        get() = LocalDiaryDimen.current
}

@Composable
public fun DiaryTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalDiaryDimen provides DiaryDimen.COMPAT,
    ) {
        MaterialExpressiveTheme(
            colorScheme = if (isSystemInDarkTheme()) {
                darkColorScheme()
            } else {
                lightColorScheme()
            },
            content = content,
        )
    }
}
