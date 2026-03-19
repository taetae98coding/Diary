package io.github.taetae98coding.diary.compose.core.snackbar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult

public suspend fun SnackbarHostState.showImmediate(message: String): SnackbarResult {
    currentSnackbarData?.dismiss()
    return showSnackbar(message)
}
