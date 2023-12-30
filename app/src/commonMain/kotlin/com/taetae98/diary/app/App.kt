package com.taetae98.diary.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.taetae98.diary.navigation.core.app.AppEntry
import com.taetae98.diary.ui.compose.scaffold.DiaryScaffold

@Composable
public fun App(
    modifier: Modifier = Modifier,
    entry: AppEntry,
) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme(),
    ) {
        DiaryScaffold(
            modifier = modifier,
            bottomBar = {
                AppBottomBar(entry = entry)
            }
        ) {
            AppNavHost(
                modifier = Modifier.padding(it),
                entry = entry,
            )
        }
    }
}