package io.github.taetae98coding.diary.feature.routine.detail

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.retain.retain
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
import io.github.taetae98coding.diary.core.model.routine.Routine
import io.github.taetae98coding.diary.core.model.routine.RoutineDetail
import io.github.taetae98coding.diary.core.model.routine.RoutineRRule
import io.github.taetae98coding.diary.feature.routine.add.component.RoutineCountEditorState
import io.github.taetae98coding.diary.feature.routine.add.component.rememberRoutineCountEditorState
import kotlinx.datetime.LocalDate

@Stable
internal class RoutineDetailScaffoldState(
    val titleCardState: TitleCardState,
    val descriptionCardState: DescriptionCardState,
    val colorCardState: ColorCardState,
    val localDateRangeCardState: LocalDateRangeCardState,
    val routineCountState: RoutineCountEditorState,
    initialRRules: List<RoutineRRule> = emptyList(),
    initialRDates: List<LocalDate> = emptyList(),
    initialExDates: List<LocalDate> = emptyList(),
) {
    val hostState: SnackbarHostState = SnackbarHostState()

    val rRuleEditorDialogState: DialogState = DialogState()

    private val _rRules = mutableStateListOf<RoutineRRule>().apply { addAll(initialRRules) }
    val rRules: List<RoutineRRule> get() = _rRules

    private val _rDates = mutableStateListOf<LocalDate>().apply { addAll(initialRDates) }
    val rDates: List<LocalDate> get() = _rDates

    private val _exDates = mutableStateListOf<LocalDate>().apply { addAll(initialExDates) }
    val exDates: List<LocalDate> get() = _exDates

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
        _rRules.add(rRule)
    }

    fun removeRule(rRule: RoutineRRule) {
        _rRules.remove(rRule)
    }

    fun addRDate(date: LocalDate) {
        if (date !in _rDates) _rDates.add(date)
    }

    fun removeRDate(date: LocalDate) {
        _rDates.remove(date)
    }

    fun addExDate(date: LocalDate) {
        if (date !in _exDates) _exDates.add(date)
    }

    fun removeExDate(date: LocalDate) {
        _exDates.remove(date)
    }
}

@Composable
internal fun rememberRoutineDetailScaffoldState(routineProvider: () -> Routine?): RoutineDetailScaffoldState {
    val routine = routineProvider()
    val detail = routine?.detail
    val key = routine?.id

    val titleCardState = rememberTitleCardState(
        inputs = arrayOf(key),
        initialText = detail?.title.orEmpty(),
    )
    val descriptionCardState = rememberDescriptionCardState(
        inputs = arrayOf(key),
        initialText = detail?.description.orEmpty(),
        initialPage = 1,
    )
    val colorCardState = rememberColorCardState(
        inputs = arrayOf(key),
        initialColor = detail?.color?.let(::Color) ?: Color.Black,
    )
    val localDateRangeCardState = rememberLocalDateRangeCardState(
        inputs = arrayOf(key),
        initialStart = detail?.start,
        initialEndInclusive = detail?.endInclusive,
    )
    val routineCountState = rememberRoutineCountEditorState(
        inputs = arrayOf(key),
        initialCount = detail?.routineCount ?: 1,
    )

    return retain(
        key,
        titleCardState,
        descriptionCardState,
        colorCardState,
        localDateRangeCardState,
        routineCountState,
    ) {
        RoutineDetailScaffoldState(
            titleCardState = titleCardState,
            descriptionCardState = descriptionCardState,
            colorCardState = colorCardState,
            localDateRangeCardState = localDateRangeCardState,
            routineCountState = routineCountState,
            initialRRules = routine?.rRules.orEmpty(),
            initialRDates = routine?.rDates.orEmpty().sorted(),
            initialExDates = routine?.exDates.orEmpty().sorted(),
        )
    }
}
