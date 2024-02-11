package com.taetae98.diary.library.compose.backhandler

import androidx.compose.runtime.Composable

@Composable
public expect fun KBackHandler(
    enabled: Boolean = true,
    onBack: () -> Unit,
)
