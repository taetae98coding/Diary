package io.github.taetae98coding.diary.core.weather.network.api.datasource

import io.github.taetae98coding.diary.core.weather.network.api.entity.WeatherRemoteEntity

public interface WeatherRemoteDataSource {
    public suspend fun getCurrent(
        latitude: Double,
        longitude: Double,
    ): WeatherRemoteEntity

    public suspend fun getForecast(
        latitude: Double,
        longitude: Double,
    ): List<WeatherRemoteEntity>
}
