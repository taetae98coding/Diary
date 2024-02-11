package com.taetae98.diary.library.compose.backhandler

import androidx.compose.runtime.Composable

@Composable
public actual fun KBackHandler(
    enabled: Boolean,
    onBack: () -> Unit,
): Unit = Unit
