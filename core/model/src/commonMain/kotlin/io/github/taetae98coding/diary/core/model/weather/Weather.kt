package io.github.taetae98coding.diary.core.model.weather

import kotlin.time.Instant

public data class Weather(
    val typeList: List<WeatherType>,
    val temperature: Double,
    val minTemperature: Double?,
    val maxTemperature: Double?,
    val instant: Instant,
)
