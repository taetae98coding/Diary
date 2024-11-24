package io.github.taetae98coding.diary.core.holiday.service

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
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
public class HolidayServiceModule {
	@Singleton
	internal fun providesHolidayService(
		@Named(HOLIDAY_API_URL)
		apiUrl: String,
		@Named(HOLIDAY_API_KEY)
		apiKey: String,
	): HolidayService {
		val json =
			Json(DefaultJson) {
				ignoreUnknownKeys = true
			}

		val client =
			HttpClient {
				expectSuccess = true

				defaultRequest {
					url(apiUrl)
					url.parameters.append("serviceKey", apiKey)
					url.parameters.append("_type", "json")
				}

				install(ContentNegotiation) {
					json(json)
				}

				install(HttpRequestRetry) {
					maxRetries = Int.MAX_VALUE
					exponentialDelay()
				}
			}

		return HolidayService(client, json)
	}

	public companion object {
		public const val HOLIDAY_API_URL: String = "HOLIDAY_API_URL"
		public const val HOLIDAY_API_KEY: String = "HOLIDAY_API_KEY"
	}
}
