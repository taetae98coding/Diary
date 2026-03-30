package io.github.taetae98coding.diary.core.weather.network.api.entity

import io.github.taetae98coding.diary.core.weather.network.api.serializer.InstantSerializer
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class WeatherRemoteEntity(
    @SerialName("weather")
    val weather: List<WeatherTypeRemoteEntity>,
    @SerialName("main")
    val main: WeahterMainRemoteEntity,
    @SerialName("dt")
    @Serializable(with = InstantSerializer::class)
    val instant: Instant,
)
