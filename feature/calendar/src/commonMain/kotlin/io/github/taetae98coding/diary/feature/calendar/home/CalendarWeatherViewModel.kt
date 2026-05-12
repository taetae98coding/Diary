@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.taetae98coding.diary.feature.calendar.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.core.model.weather.Weather
import io.github.taetae98coding.diary.domain.weather.usecase.FetchCurrentWeatherUseCase
import io.github.taetae98coding.diary.domain.weather.usecase.GetCurrentWeatherUseCase
import kotlin.math.abs
import kotlin.time.Clock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import org.koin.core.annotation.KoinViewModel
import org.koin.core.annotation.Provided

@KoinViewModel
internal class CalendarWeatherViewModel(
    @Provided private val clock: Clock,
    getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val fetchCurrentWeatherUseCase: FetchCurrentWeatherUseCase,
) : ViewModel() {
    val weather: StateFlow<List<CalendarWeatherUiState>> = getCurrentWeatherUseCase()
        .mapLatest { it.getOrDefault(emptyList()) }
        .mapLatest { list ->
            val timeZone = TimeZone.currentSystemDefault()
            val now = clock.now()
            val today = clock.todayIn(timeZone)

            list.groupBy { it.instant.toLocalDateTime(timeZone).date }
                .mapValues { entry ->
                    val roundWeather = entry.value.minBy { abs(now.toEpochMilliseconds() - it.instant.toEpochMilliseconds()) }

                    Weather(
                        typeList = entry.value.flatMap { it.typeList },
                        temperature = roundWeather.temperature,
                        minTemperature = entry.value.mapNotNull { it.minTemperature }.minOrNull(),
                        maxTemperature = entry.value.mapNotNull { it.maxTemperature }.maxOrNull(),
                        instant = roundWeather.instant,
                    )
                }.map { entry ->
                    CalendarWeatherUiState(
                        isToday = entry.key == today,
                        typeList = entry.value.typeList.distinctBy { it.icon },
                        temperature = entry.value.temperature,
                        minTemperature = entry.value.minTemperature,
                        maxTemperature = entry.value.maxTemperature,
                        localDate = entry.key,
                    )
                }.let {
                    if (it.size > 1) {
                        it.subList(0, it.lastIndex - 1)
                    } else {
                        it
                    }
                }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    fun fetch() {
        viewModelScope.launch {
            fetchCurrentWeatherUseCase()
        }
    }
}
