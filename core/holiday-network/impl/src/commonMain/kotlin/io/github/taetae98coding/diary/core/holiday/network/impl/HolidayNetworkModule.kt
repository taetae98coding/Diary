package io.github.taetae98coding.diary.core.holiday.network.impl

import io.github.taetae98coding.diary.core.holiday.network.impl.di.HolidayHttpClient
import io.github.taetae98coding.diary.core.holiday.network.impl.di.HolidayHttpEngine
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
public class HolidayNetworkModule {
    @HolidayHttpClient
    @Single
    internal fun providesHolidayHttpClient(@HolidayHttpEngine engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            defaultRequest {
                url("https://taetae98coding.github.io/Holiday/")
            }

            install(ContentNegotiation) {
                json(
                    Json(DefaultJson) {
                        ignoreUnknownKeys = true
                    },
                )
            }
        }
    }
}
