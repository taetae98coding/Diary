package io.github.taetae98coding.diary.domain.weather.repository

import io.github.taetae98coding.diary.core.model.weather.Weather
import kotlinx.coroutines.flow.Flow

public interface WeatherRepository {
    public fun getCurrentWeather(): Flow<List<Weather>>
    public suspend fun fetchCurrentWeather()
}
