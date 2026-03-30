package io.github.taetae98coding.diary.core.weather.network.impl

import io.github.taetae98coding.diary.core.weather.network.impl.di.WeatherApiKey
import io.github.taetae98coding.diary.core.weather.network.impl.di.WeatherHttpClient
import io.github.taetae98coding.diary.core.weather.network.impl.di.WeatherHttpEngine
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.DefaultJson
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan
@Configuration
public class WeatherNetworkModule {
    @WeatherHttpClient
    @Single
    internal fun providesWeatherHttpClient(
        @WeatherHttpEngine
        engine: HttpClientEngine,
        @WeatherApiKey
        apiKey: String,
    ): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(
                    Json(DefaultJson) {
                        ignoreUnknownKeys = true
                    },
                )
            }

            defaultRequest {
                url("https://api.openweathermap.org/")
                url.parameters.append("appid", apiKey)
                url.parameters.append("units", "metric")
                url.parameters.append("lang", "kr")
            }
        }
    }
}
