package io.github.taetae98coding.diary.feature.memo.detail

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.graphics.toArgb
import io.github.taetae98coding.diary.core.design.system.diary.color.DiaryColorState
import io.github.taetae98coding.diary.core.design.system.diary.component.DiaryComponentState
import io.github.taetae98coding.diary.core.design.system.diary.date.DiaryDateState
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal sealed class MemoDetailScreenState {
	protected abstract val coroutineScope: CoroutineScope

	abstract val componentState: DiaryComponentState
	abstract val dateState: DiaryDateState
	abstract val colorState: DiaryColorState

	private var messageJob: Job? = null

	val hostState: SnackbarHostState = SnackbarHostState()

	data class Add(
		override val coroutineScope: CoroutineScope,
		override val componentState: DiaryComponentState,
		override val dateState: DiaryDateState,
		override val colorState: DiaryColorState,
	) : MemoDetailScreenState()

	data class Detail(
		val onDelete: () -> Unit,
		val onUpdate: () -> Unit,
		override val coroutineScope: CoroutineScope,
		override val componentState: DiaryComponentState,
		override val dateState: DiaryDateState,
		override val colorState: DiaryColorState,
	) : MemoDetailScreenState()

	val memoDetail: MemoDetail
		get() {
			return MemoDetail(
				title = componentState.title,
				description = componentState.description,
				start = dateState.start.takeIf { dateState.hasDate },
				endInclusive = dateState.endInclusive.takeIf { dateState.hasDate },
				color = colorState.color.toArgb(),
			)
		}

	fun requestTitleFocus() {
		componentState.requestTitleFocus()
	}

	fun titleError() {
		requestTitleFocus()
		componentState.titleError()
	}

	fun showMessage(message: String) {
		messageJob?.cancel()
		messageJob = coroutineScope.launch { hostState.showSnackbar(message) }
	}

	fun clearInput() {
		componentState.clearInput()
	}
}
