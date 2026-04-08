package io.github.taetae98coding.diary.feature.more.goldenholiday

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.domain.holiday.usecase.FetchHolidayUseCase
import io.github.taetae98coding.diary.domain.holiday.usecase.GetGoldenHolidayUseCase
import io.github.taetae98coding.diary.domain.holiday.usecase.GetHolidayUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class GoldenHolidayViewModel(
    @param:InjectedParam
    private val year: Int,
    private val fetchHolidayUseCase: FetchHolidayUseCase,
    private val getHolidayUseCase: GetHolidayUseCase,
    private val getGoldenHolidayUseCase: GetGoldenHolidayUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<GoldenHolidayScaffoldUiState>(GoldenHolidayScaffoldUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun fetch(annualLeave: Int) {
        val currentAnnualLeave = when (val uiState = uiState.value) {
            is GoldenHolidayScaffoldUiState.Loading -> uiState.annualLeave
            is GoldenHolidayScaffoldUiState.State -> uiState.annualLeave
            else -> null
        }

        if (currentAnnualLeave == annualLeave) {
            return
        }

        viewModelScope.launch {
            val fetchHolidayResult = fetchHolidayUseCase(year)
            val holidayList = getHolidayUseCase(year).first().getOrDefault(emptyList())

            if (fetchHolidayResult.isFailure && holidayList.isEmpty()) {
                _uiState.value = GoldenHolidayScaffoldUiState.UnknownError
                return@launch
            }

            if (fetchHolidayResult.isSuccess && holidayList.isEmpty()) {
                _uiState.value = GoldenHolidayScaffoldUiState.HolidayNotExist
                return@launch
            }

            val getGoldenHolidayResult = getGoldenHolidayUseCase(year, annualLeave).first()
            if (getGoldenHolidayResult.isFailure) {
                _uiState.value = GoldenHolidayScaffoldUiState.UnknownError
                return@launch
            }

            _uiState.value = GoldenHolidayScaffoldUiState.State(
                annualLeave = annualLeave,
                goldenHolidayList = getGoldenHolidayResult.getOrDefault(emptyList()),
            )
        }
    }
}
