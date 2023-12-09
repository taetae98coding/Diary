package com.taetae98.diary.feature.more

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.taetae98.diary.navigation.core.more.MoreEntry

@Composable
public fun MoreRoute(
    modifier: Modifier = Modifier,
    entry: MoreEntry
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "More")
    }
}