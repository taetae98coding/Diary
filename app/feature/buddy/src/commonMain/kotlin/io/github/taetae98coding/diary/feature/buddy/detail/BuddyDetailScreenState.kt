package io.github.taetae98coding.diary.feature.buddy.detail

import androidx.compose.material3.DrawerState
import androidx.compose.material3.SnackbarHostState
import io.github.taetae98coding.diary.core.design.system.diary.component.DiaryComponentState
import io.github.taetae98coding.diary.core.model.buddy.BuddyGroupDetail
import io.github.taetae98coding.diary.feature.buddy.common.BuddyBottomSheetState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal sealed class BuddyDetailScreenState {
    protected abstract val coroutineScope: CoroutineScope

    abstract val componentState: DiaryComponentState
    abstract val buddyBottomSheetState: BuddyBottomSheetState

    private var messageJob: Job? = null

    val hostState: SnackbarHostState = SnackbarHostState()

    data class Add(
        override val coroutineScope: CoroutineScope,
        override val componentState: DiaryComponentState,
        override val buddyBottomSheetState: BuddyBottomSheetState,
    ) : BuddyDetailScreenState()

    data class Detail(
        val onCalendar: () -> Unit,
        override val coroutineScope: CoroutineScope,
        val drawerState: DrawerState,
        override val componentState: DiaryComponentState,
        override val buddyBottomSheetState: BuddyBottomSheetState,
    ) : BuddyDetailScreenState()

    val detail: BuddyGroupDetail
        get() {
            return BuddyGroupDetail(
                title = componentState.title,
                description = componentState.description,
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
