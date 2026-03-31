package io.github.taetae98coding.diary.feature.more.goldenholiday

import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import io.github.taetae98coding.diary.compose.core.dialog.DialogState
import io.github.taetae98coding.diary.compose.core.dialog.rememberDialogState
import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

@Stable
internal class GoldenHolidayScaffoldState(
    val dialogState: DialogState,
    val pagerState: PagerState,
) {
    var annualLeave by mutableStateOf(0)
        private set

    var sortOrder by mutableStateOf(GoldenHolidaySortOrder.LONGEST_FIRST)
        private set

    fun minusAnnualLeave() {
        annualLeave = (annualLeave - 1).coerceAtLeast(0)
    }

    fun plusAnnualLeave() {
        annualLeave++
    }

    fun updateSortOrder(order: GoldenHolidaySortOrder) {
        sortOrder = order
    }

    companion object {
        fun saver(
            dialogState: DialogState,
            pagerState: PagerState,
        ): Saver<GoldenHolidayScaffoldState, Any> {
            return listSaver(
                save = {
                    listOf(
                        it.annualLeave,
                        it.sortOrder.name,
                    )
                },
                restore = {
                    GoldenHolidayScaffoldState(
                        dialogState = dialogState,
                        pagerState = pagerState,
                    ).apply {
                        annualLeave = it[0] as Int
                        sortOrder = GoldenHolidaySortOrder.valueOf(it[1] as String)
                    }
                },
            )
        }
    }
}

@Composable
internal fun rememberGoldenHolidayScaffoldState(initialPage: Int = remember { Clock.System.todayIn(TimeZone.currentSystemDefault()).year }): GoldenHolidayScaffoldState {
    val dialogState = rememberDialogState()
    val pagerState = rememberPagerState(initialPage = initialPage) { Int.MAX_VALUE }

    return rememberSaveable(
        saver = GoldenHolidayScaffoldState.saver(dialogState, pagerState),
    ) {
        GoldenHolidayScaffoldState(
            dialogState = dialogState,
            pagerState = pagerState,
        )
    }
}
