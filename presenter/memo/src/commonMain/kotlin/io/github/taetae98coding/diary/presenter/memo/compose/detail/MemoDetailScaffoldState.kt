package io.github.taetae98coding.diary.presenter.memo.compose.detail

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import io.github.taetae98coding.diary.compose.core.card.ColorCardState
import io.github.taetae98coding.diary.compose.core.card.DateRangeCardState
import io.github.taetae98coding.diary.compose.core.card.DescriptionCardState
import io.github.taetae98coding.diary.compose.core.card.TitleCardState
import io.github.taetae98coding.diary.compose.core.card.rememberColorCardState
import io.github.taetae98coding.diary.compose.core.card.rememberDateRangeCardState
import io.github.taetae98coding.diary.compose.core.card.rememberDescriptionCardState
import io.github.taetae98coding.diary.compose.core.card.rememberTitleCardState
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

public class MemoDetailScaffoldState(
    public val titleCardState: TitleCardState,
    public val descriptionCardState: DescriptionCardState,
    public val dateRangeCardState: DateRangeCardState,
    public val colorCardState: ColorCardState,
) {
    public val hostState: SnackbarHostState = SnackbarHostState()

    public val detail: MemoDetail
        get() = MemoDetail(
            title = titleCardState.textFieldState.text.toString(),
            description = descriptionCardState.textFieldState.text.toString(),
            isAllDay = dateRangeCardState.isAllDay,
            localDateTimeRange = dateRangeCardState.localDateRange,
            color = colorCardState.value.toArgb(),
        )
}

@Composable
public fun rememberMemoDetailScaffoldState(memoProvider: () -> Memo?): MemoDetailScaffoldState {
    val memo = memoProvider()
    val detail = memo?.detail
    val start = remember(memo?.id) {
        detail?.localDateTimeRange?.start ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }
    val endInclusive = remember(memo?.id) {
        detail?.localDateTimeRange?.endInclusive ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }

    val titleCardState = rememberTitleCardState(
        inputs = arrayOf(memo?.id),
        initialText = detail?.title.orEmpty(),
    )
    val descriptionCardState = rememberDescriptionCardState(
        inputs = arrayOf(memo?.id),
        initialText = detail?.description.orEmpty(),
        initialPage = 1,
    )
    val dateRangeCardState = rememberDateRangeCardState(
        inputs = arrayOf(memo?.id),
        initialHasDateRange = detail?.localDateTimeRange != null,
        initialIsAllDay = detail?.isAllDay ?: false,
        initialStart = start,
        initialEndInclusive = endInclusive,
    )
    val colorCardState = rememberColorCardState(
        inputs = arrayOf(memo?.id),
        initialColor = detail?.color?.let(::Color) ?: Color.Black,
    )

    return retain(titleCardState, descriptionCardState, dateRangeCardState, colorCardState) {
        MemoDetailScaffoldState(
            titleCardState = titleCardState,
            descriptionCardState = descriptionCardState,
            dateRangeCardState = dateRangeCardState,
            colorCardState = colorCardState,
        )
    }
}
