package com.taetae98.diary.data.remote.impl.holiday

import com.taetae98.diary.data.remote.impl.getProxyUrl
import io.ktor.client.HttpClient
import io.ktor.client.engine.ProxyBuilder
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.Url
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Singleton

@Module
internal class HolidayModule {
    @Singleton
    @Named(HOLIDAY_JSON)
    fun providesHolidayJson(): Json {
        return Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
            coerceInputValues = true
            allowStructuredMapKeys = true
            allowSpecialFloatingPointValues = true
        }
    }

    @Singleton
    @Named(HOLIDAY_CLIENT)
    fun providesHolidayClient(
        @Named(HOLIDAY_JSON)
        json: Json
    ): HttpClient {
        return HttpClient {
            expectSuccess = true

            engine {
                getProxyUrl()?.let { proxy = ProxyBuilder.http(Url(it)) }
            }

            defaultRequest {
                url(getHolidayUrl())
                url.parameters.append("serviceKey", getHolidayKey())
                url.parameters.append("_type", "json")
            }

            install(HttpCache)
            install(ContentNegotiation) {
                json(json)
            }
        }
    }

    companion object {
        const val HOLIDAY_CLIENT = "holidayClient"
        const val HOLIDAY_JSON = "holidayJson"
    }
}