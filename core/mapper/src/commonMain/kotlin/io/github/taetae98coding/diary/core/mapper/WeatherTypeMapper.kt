package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.model.weather.WeatherType
import io.github.taetae98coding.diary.core.weather.network.api.entity.WeatherTypeRemoteEntity

public fun WeatherTypeRemoteEntity.toDomain(): WeatherType {
    return WeatherType(
        icon = icon.image,
        description = description,
    )
}
