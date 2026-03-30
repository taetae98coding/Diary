package io.github.taetae98coding.diary.core.weather.network.api.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class WeatherTypeRemoteEntity(
    @SerialName("icon")
    val icon: WeatherTypeIconRemoteEntity,
    @SerialName("description")
    val description: String,
)
