package io.github.taetae98coding.diary.presenter.calendar.api

import io.github.taetae98coding.diary.core.model.weather.WeatherType
import kotlinx.datetime.LocalDate

public data class CalendarWeatherUiState(
    val isToday: Boolean,
    val typeList: List<WeatherType>,
    val temperature: Double,
    val minTemperature: Double?,
    val maxTemperature: Double?,
    val localDate: LocalDate,
)
