package com.taetae98.diary.feature.memo

import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape

internal object MemoDefaults {
    val shape: Shape
        @Composable
        get() = CardDefaults.shape
}