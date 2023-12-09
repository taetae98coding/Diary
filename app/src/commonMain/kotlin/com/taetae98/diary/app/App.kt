package com.taetae98.diary.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.taetae98.diary.navigation.core.app.AppEntry
import com.taetae98.diary.ui.compose.modifier.scaffoldPadding

@Composable
public fun App(
    modifier: Modifier = Modifier,
    entry: AppEntry,
) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme(),
    ) {
        Scaffold(
            modifier = modifier,
        ) {
            AppNavHost(
                modifier = Modifier.scaffoldPadding(it),
                entry = entry,
            )
        }
    }
}