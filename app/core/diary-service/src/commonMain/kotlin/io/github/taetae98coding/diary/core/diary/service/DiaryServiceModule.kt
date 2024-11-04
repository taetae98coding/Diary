package io.github.taetae98coding.diary.core.diary.service

import io.github.taetae98coding.diary.core.account.preferences.AccountPreferences
import io.github.taetae98coding.diary.core.diary.service.plugin.DiaryClientTokenPlugin
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.DefaultJson
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Singleton

@Module
@ComponentScan
public class DiaryServiceModule {
    @Singleton
    @Named(DIARY_JSON)
    internal fun providesDiaryJson(): Json {
        return Json(DefaultJson) {
            ignoreUnknownKeys = true
        }
    }

    @Singleton
    @Named(DIARY_CLIENT)
    internal fun providesDiaryClient(
        accountPreferences: AccountPreferences,
        @Named(DIARY_JSON)
        json: Json,
        @Named(DIARY_API_URL)
        apiUrl: String,
    ): HttpClient {
        return HttpClient {
            defaultRequest {
                url(apiUrl)
            }

            install(ContentNegotiation) {
                json(json)
            }

            install(DiaryClientTokenPlugin) {
                preferences = accountPreferences
            }
        }
    }

    public companion object {
        internal const val DIARY_JSON = "DIARY_JSON"
        internal const val DIARY_CLIENT = "DIARY_CLIENT"

        public const val DIARY_API_URL: String = "DIARY_API_URL"
    }
}
