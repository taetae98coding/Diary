package io.github.taetae98coding.diary.core.compose.tag.detail

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.graphics.toArgb
import io.github.taetae98coding.diary.core.design.system.diary.color.DiaryColorState
import io.github.taetae98coding.diary.core.design.system.diary.component.DiaryComponentState
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

public sealed class TagDetailScaffoldState {
	protected abstract val coroutineScope: CoroutineScope

	internal abstract val componentState: DiaryComponentState
	internal abstract val colorState: DiaryColorState

	private var messageJob: Job? = null

	internal val hostState: SnackbarHostState = SnackbarHostState()

	public data class Add(
		override val coroutineScope: CoroutineScope,
		override val componentState: DiaryComponentState,
		override val colorState: DiaryColorState,
	) : TagDetailScaffoldState()

	public data class Detail(
		val onUpdate: () -> Unit,
		val onDelete: () -> Unit,
		val navigateToMemo: () -> Unit,
		override val coroutineScope: CoroutineScope,
		override val componentState: DiaryComponentState,
		override val colorState: DiaryColorState,
	) : TagDetailScaffoldState()

	public val tagDetail: TagDetail
		get() {
			return TagDetail(
				title = componentState.title,
				description = componentState.description,
				color = colorState.color.toArgb(),
			)
		}

	internal fun requestTitleFocus() {
		componentState.requestTitleFocus()
	}

	internal fun clearInput() {
		componentState.clearInput()
	}

	internal fun titleError() {
		componentState.titleError()
	}

	internal fun showMessage(message: String) {
		messageJob?.cancel()
		messageJob = coroutineScope.launch { hostState.showSnackbar(message) }
	}
}
