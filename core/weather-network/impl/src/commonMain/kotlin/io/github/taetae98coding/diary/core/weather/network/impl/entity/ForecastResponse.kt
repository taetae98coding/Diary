package io.github.taetae98coding.diary.core.weather.network.impl.entity

import io.github.taetae98coding.diary.core.weather.network.api.entity.WeatherRemoteEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ForecastResponse(
    @SerialName("list")
    val list: List<WeatherRemoteEntity>,
)
