package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.model.weather.Weather
import io.github.taetae98coding.diary.core.weather.network.api.entity.WeatherRemoteEntity
import io.github.taetae98coding.diary.core.weather.network.api.entity.WeatherTypeRemoteEntity

public fun WeatherRemoteEntity.toDomain(): Weather {
    return Weather(
        typeList = weather.map(WeatherTypeRemoteEntity::toDomain),
        temperature = main.temperature,
        minTemperature = main.minTemperature,
        maxTemperature = main.maxTemperature,
        instant = instant,
    )
}
