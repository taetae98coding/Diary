package io.github.taetae98coding.diary.feature.more.goldenholiday

import io.github.taetae98coding.diary.core.model.holiday.GoldenHoliday

internal sealed interface GoldenHolidayScaffoldUiState {
    data object Idle : GoldenHolidayScaffoldUiState
    data class Loading(val annualLeave: Int) : GoldenHolidayScaffoldUiState

    data class State(
        val annualLeave: Int,
        val goldenHolidayList: List<GoldenHoliday>,
    ) : GoldenHolidayScaffoldUiState

    data object HolidayNotExist : GoldenHolidayScaffoldUiState

    data object UnknownError : GoldenHolidayScaffoldUiState
}
