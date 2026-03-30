package io.github.taetae98coding.diary.core.weather.network.api.entity

import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
public value class WeatherTypeIconRemoteEntity(internal val value: String) {
    public val iconId: String
        get() = value.replace("n", "d")

    public val image: String
        get() = "https://openweathermap.org/img/wn/$iconId@2x.png"
}
