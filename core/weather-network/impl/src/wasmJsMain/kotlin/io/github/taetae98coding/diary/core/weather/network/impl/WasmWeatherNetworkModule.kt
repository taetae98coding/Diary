package io.github.taetae98coding.diary.core.weather.network.impl

import io.github.taetae98coding.diary.core.weather.network.impl.di.WeatherHttpEngine
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
public class WasmWeatherNetworkModule {
    @WeatherHttpEngine
    @Single
    internal fun providesWeatherHttpEngine(): HttpClientEngine {
        return Js.create()
    }
}
