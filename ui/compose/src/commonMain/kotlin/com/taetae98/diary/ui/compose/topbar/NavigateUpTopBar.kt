package com.taetae98.diary.ui.compose.topbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.taetae98.diary.ui.compose.icon.NavigateUpIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun NavigateUpTopBar(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = {},
        navigationIcon = {
            IconButton(onClick = onNavigateUp) {
                NavigateUpIcon()
            }
        }
    )
}