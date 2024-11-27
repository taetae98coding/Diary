package io.github.taetae98coding.diary.feature.tag.list

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

internal class TagListScreenState(
	private val coroutineScope: CoroutineScope,
) {
	private var messageJob: Job? = null

	val hostState: SnackbarHostState = SnackbarHostState()

	fun showMessage(
		message: String,
		actionLabel: String,
		onResult: (SnackbarResult) -> Unit,
	) {
		messageJob?.cancel()
		messageJob =
			coroutineScope.launch {
				val result =
					hostState.showSnackbar(
						message = message,
						actionLabel = actionLabel,
						duration = SnackbarDuration.Long,
					)

				if (isActive) {
					onResult(result)
				}
			}
	}

	fun showMessage(
		message: String,
	) {
		messageJob?.cancel()
		messageJob = coroutineScope.launch { hostState.showSnackbar(message = message) }
	}
}
