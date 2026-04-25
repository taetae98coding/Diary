package io.github.taetae98coding.diary.feature.routine.add

import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import io.github.taetae98coding.diary.compose.core.card.ColorCardState
import io.github.taetae98coding.diary.compose.core.card.DescriptionCardState
import io.github.taetae98coding.diary.compose.core.card.LocalDateRangeCardState
import io.github.taetae98coding.diary.compose.core.card.TitleCardState
import io.github.taetae98coding.diary.compose.core.card.rememberColorCardState
import io.github.taetae98coding.diary.compose.core.card.rememberDescriptionCardState
import io.github.taetae98coding.diary.compose.core.card.rememberLocalDateRangeCardState
import io.github.taetae98coding.diary.compose.core.card.rememberTitleCardState
import io.github.taetae98coding.diary.compose.core.dialog.DialogState
import io.github.taetae98coding.diary.core.model.routine.RoutineDetail
import io.github.taetae98coding.diary.core.model.routine.RoutineRRule
import io.github.taetae98coding.diary.feature.routine.add.component.RoutineCountEditorState
import io.github.taetae98coding.diary.feature.routine.add.component.rememberRoutineCountEditorState
import io.github.taetae98coding.diary.library.compose.ui.random
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Stable
internal class RoutineAddScaffoldState(
    val titleCardState: TitleCardState,
    val descriptionCardState: DescriptionCardState,
    val colorCardState: ColorCardState,
    val localDateRangeCardState: LocalDateRangeCardState,
    val routineCountState: RoutineCountEditorState,
) {
    val hostState: SnackbarHostState = SnackbarHostState()

    val rRules: SnapshotStateList<RoutineRRule> = mutableStateListOf()

    val rRuleEditorDialogState: DialogState = DialogState()

    val detail: RoutineDetail
        get() = RoutineDetail(
            title = titleCardState.textFieldState.text.toString(),
            description = descriptionCardState.textFieldState.text.toString(),
            start = localDateRangeCardState.start,
            endInclusive = localDateRangeCardState.endInclusive,
            color = colorCardState.value.toArgb(),
            routineCount = routineCountState.count,
        )

    fun showRRuleDialog() {
        rRuleEditorDialogState.show()
    }

    fun hideRRuleDialog() {
        rRuleEditorDialogState.hide()
    }

    fun addRRule(rRule: RoutineRRule) {
        if (rRule.diaryByDay.days.isEmpty() && rRule.byMonthDay.isEmpty()) {
            return
        }
        rRules.add(rRule)
    }

    fun removeRule(rRule: RoutineRRule) {
        rRules.remove(rRule)
    }

    suspend fun reset() {
        coroutineScope {
            launch { colorCardState.updateColor(Color.random()) }
            titleCardState.textFieldState.clearText()
            descriptionCardState.textFieldState.clearText()
            localDateRangeCardState.updateStart(null)
            localDateRangeCardState.updateEndInclusive(null)
            routineCountState.updateCount(1)
            rRules.clear()
        }
    }
}

@Composable
internal fun rememberRoutineAddScaffoldState(): RoutineAddScaffoldState {
    val titleCardState = rememberTitleCardState()
    val descriptionCardState = rememberDescriptionCardState()
    val colorCardState = rememberColorCardState()
    val localDateRangeCardState = rememberLocalDateRangeCardState()
    val routineCountState = rememberRoutineCountEditorState()

    return retain(
        titleCardState,
        descriptionCardState,
        colorCardState,
        localDateRangeCardState,
        routineCountState,
    ) {
        RoutineAddScaffoldState(
            titleCardState = titleCardState,
            descriptionCardState = descriptionCardState,
            colorCardState = colorCardState,
            localDateRangeCardState = localDateRangeCardState,
            routineCountState = routineCountState,
        )
    }
}
