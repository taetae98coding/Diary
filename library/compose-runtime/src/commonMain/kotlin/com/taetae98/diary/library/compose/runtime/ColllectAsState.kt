package com.taetae98.diary.library.compose.runtime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import kotlinx.coroutines.flow.StateFlow

@Composable
public expect fun <T> StateFlow<T>.collectAsStateOnLifecycle(): State<T>