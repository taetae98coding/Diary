package io.github.taetae98coding.diary.core.weather.network.api.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class WeahterMainRemoteEntity(
    @SerialName("temp")
    val temperature: Double,
    @SerialName("temp_min")
    val minTemperature: Double?,
    @SerialName("temp_max")
    val maxTemperature: Double?,
)
