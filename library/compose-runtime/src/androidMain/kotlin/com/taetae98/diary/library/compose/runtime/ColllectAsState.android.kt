package com.taetae98.diary.library.compose.runtime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow

@Composable
public actual fun <T> StateFlow<T>.collectAsStateOnLifecycle(): State<T> {
    return collectAsStateWithLifecycle()
}