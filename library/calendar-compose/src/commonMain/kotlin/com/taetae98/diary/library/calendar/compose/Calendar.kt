package com.taetae98.diary.library.calendar.compose

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
public fun Calendar(
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = "달력"
    )
}