package io.github.taetae98coding.diary.presenter.calendar.api

import io.github.taetae98coding.diary.core.model.weather.Weather
import io.github.taetae98coding.diary.domain.weather.usecase.FetchCurrentWeatherUseCase
import io.github.taetae98coding.diary.domain.weather.usecase.GetCurrentWeatherUseCase
import kotlin.math.abs
import kotlin.time.Clock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

public class CalendarWeatherStateHolder(
    private val clock: Clock,
    private val coroutineScope: CoroutineScope,
    getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val fetchCurrentWeatherUseCase: FetchCurrentWeatherUseCase,
) {
    public val weather: StateFlow<List<CalendarWeatherUiState>> = getCurrentWeatherUseCase()
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
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    public fun fetch() {
        coroutineScope.launch {
            fetchCurrentWeatherUseCase()
        }
    }
}
