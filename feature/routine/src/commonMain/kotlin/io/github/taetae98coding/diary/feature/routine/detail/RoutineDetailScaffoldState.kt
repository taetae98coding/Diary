package io.github.taetae98coding.diary.feature.routine.detail

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import io.github.taetae98coding.diary.compose.core.card.ColorCardState
import io.github.taetae98coding.diary.compose.core.card.DescriptionCardState
import io.github.taetae98coding.diary.compose.core.card.TitleCardState
import io.github.taetae98coding.diary.compose.core.card.rememberColorCardState
import io.github.taetae98coding.diary.compose.core.card.rememberDescriptionCardState
import io.github.taetae98coding.diary.compose.core.card.rememberTitleCardState
import io.github.taetae98coding.diary.core.model.routine.Routine
import io.github.taetae98coding.diary.core.model.routine.RoutineDetail
import io.github.taetae98coding.diary.feature.routine.add.card.RoutineCountCardState
import io.github.taetae98coding.diary.feature.routine.add.card.RoutineDateRangeCardState
import io.github.taetae98coding.diary.feature.routine.add.card.rememberRoutineCountCardState
import io.github.taetae98coding.diary.feature.routine.add.card.rememberRoutineDateRangeCardState

internal class RoutineDetailScaffoldState(
    val titleCardState: TitleCardState,
    val descriptionCardState: DescriptionCardState,
    val colorCardState: ColorCardState,
    val dateRangeCardState: RoutineDateRangeCardState,
    val routineCountCardState: RoutineCountCardState,
    val lazyListState: LazyListState,
) {
    val hostState: SnackbarHostState = SnackbarHostState()

    private var previousFirstVisibleItemIndex by mutableIntStateOf(0)
    private var previousFirstVisibleItemScrollOffset by mutableIntStateOf(0)

    val isFabVisible: Boolean by derivedStateOf {
        val currentIndex = lazyListState.firstVisibleItemIndex
        val currentOffset = lazyListState.firstVisibleItemScrollOffset

        val isScrollingUp = currentIndex < previousFirstVisibleItemIndex ||
            (currentIndex == previousFirstVisibleItemIndex && currentOffset <= previousFirstVisibleItemScrollOffset)

        previousFirstVisibleItemIndex = currentIndex
        previousFirstVisibleItemScrollOffset = currentOffset

        isScrollingUp
    }

    val detail: RoutineDetail
        get() = RoutineDetail(
            title = titleCardState.textFieldState.text.toString(),
            description = descriptionCardState.textFieldState.text.toString(),
            start = dateRangeCardState.start,
            endInclusive = dateRangeCardState.endInclusive,
            color = colorCardState.value.toArgb(),
            routineCount = routineCountCardState.count,
        )
}

@Composable
internal fun rememberRoutineDetailScaffoldState(routineProvider: () -> Routine?): RoutineDetailScaffoldState {
    val routine = routineProvider()
    val detail = routine?.detail

    val titleCardState = rememberTitleCardState(
        inputs = arrayOf(routine?.id),
        initialText = detail?.title.orEmpty(),
    )
    val descriptionCardState = rememberDescriptionCardState(
        inputs = arrayOf(routine?.id),
        initialText = detail?.description.orEmpty(),
        initialPage = 1,
    )
    val colorCardState = rememberColorCardState(
        inputs = arrayOf(routine?.id),
        initialColor = detail?.color?.let(::Color) ?: Color.Black,
    )
    val routineCountCardState = rememberRoutineCountCardState(
        inputs = arrayOf(routine?.id),
        initialCount = detail?.routineCount ?: 1,
    )
    val dateRangeCardState = rememberRoutineDateRangeCardState(
        inputs = arrayOf(routine?.id),
        initialStart = detail?.start,
        initialEndInclusive = detail?.endInclusive,
    )
    val lazyListState = rememberLazyListState()

    return retain(titleCardState, descriptionCardState, colorCardState, routineCountCardState, dateRangeCardState, lazyListState) {
        RoutineDetailScaffoldState(
            titleCardState = titleCardState,
            descriptionCardState = descriptionCardState,
            colorCardState = colorCardState,
            routineCountCardState = routineCountCardState,
            dateRangeCardState = dateRangeCardState,
            lazyListState = lazyListState,
        )
    }
}
