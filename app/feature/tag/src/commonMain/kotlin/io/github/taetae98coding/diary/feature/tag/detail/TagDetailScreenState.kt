package io.github.taetae98coding.diary.feature.tag.detail

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.graphics.toArgb
import io.github.taetae98coding.diary.core.design.system.diary.color.DiaryColorState
import io.github.taetae98coding.diary.core.design.system.diary.component.DiaryComponentState
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal sealed class TagDetailScreenState {
	protected abstract val coroutineScope: CoroutineScope

	abstract val componentState: DiaryComponentState
	abstract val colorState: DiaryColorState

	private var messageJob: Job? = null

	val hostState: SnackbarHostState = SnackbarHostState()

	data class Add(
		override val coroutineScope: CoroutineScope,
		override val componentState: DiaryComponentState,
		override val colorState: DiaryColorState,
	) : TagDetailScreenState()

	data class Detail(
		val onUpdate: () -> Unit,
		val onDelete: () -> Unit,
		val onMemo: () -> Unit,
		override val coroutineScope: CoroutineScope,
		override val componentState: DiaryComponentState,
		override val colorState: DiaryColorState,
	) : TagDetailScreenState()

	val tagDetail: TagDetail
		get() {
			return TagDetail(
				title = componentState.title,
				description = componentState.description,
				color = colorState.color.toArgb(),
			)
		}

	fun requestTitleFocus() {
		componentState.requestTitleFocus()
	}

	fun clearInput() {
		componentState.clearInput()
	}

	fun titleError() {
		componentState.titleError()
	}

	fun showMessage(message: String) {
		messageJob?.cancel()
		messageJob = coroutineScope.launch { hostState.showSnackbar(message) }
	}
}
