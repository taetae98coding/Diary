package com.taetae98.diary.feature.memo.detail

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.taetae98.diary.ui.compose.topbar.NavigateUpTopBar

@Composable
internal fun MemoDetailScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = { NavigateUpTopBar(onNavigateUp = onNavigateUp) }
    ) {

    }
}
