package io.github.taetae98coding.diary.core.compose.memo.detail

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.graphics.toArgb
import io.github.taetae98coding.diary.core.design.system.diary.color.DiaryColorState
import io.github.taetae98coding.diary.core.design.system.diary.component.DiaryComponentState
import io.github.taetae98coding.diary.core.design.system.diary.date.DiaryDateState
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

public sealed class MemoDetailScaffoldState {
    protected abstract val coroutineScope: CoroutineScope

    internal abstract val componentState: DiaryComponentState
    internal abstract val dateState: DiaryDateState
    internal abstract val colorState: DiaryColorState

    private var messageJob: Job? = null

    internal val hostState: SnackbarHostState = SnackbarHostState()

    public data class Add(
        override val coroutineScope: CoroutineScope,
        override val componentState: DiaryComponentState,
        override val dateState: DiaryDateState,
        override val colorState: DiaryColorState,
    ) : MemoDetailScaffoldState()

    public data class Detail(
        val onDelete: () -> Unit,
        val onUpdate: () -> Unit,
        override val coroutineScope: CoroutineScope,
        override val componentState: DiaryComponentState,
        override val dateState: DiaryDateState,
        override val colorState: DiaryColorState,
    ) : MemoDetailScaffoldState()

    public val detail: MemoDetail
        get() {
            return MemoDetail(
                title = componentState.title,
                description = componentState.description,
                start = dateState.start.takeIf { dateState.hasDate },
                endInclusive = dateState.endInclusive.takeIf { dateState.hasDate },
                color = colorState.color.toArgb(),
            )
        }

    internal fun requestTitleFocus() {
        componentState.requestTitleFocus()
    }

    internal fun titleError() {
        requestTitleFocus()
        componentState.titleError()
    }

    internal fun showMessage(message: String) {
        messageJob?.cancel()
        messageJob = coroutineScope.launch { hostState.showSnackbar(message) }
    }

    internal fun clearInput() {
        componentState.clearInput()
    }
}
