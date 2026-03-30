package io.github.taetae98coding.diary.core.weather.network.impl.datasource

import io.github.taetae98coding.diary.core.weather.network.api.datasource.WeatherRemoteDataSource
import io.github.taetae98coding.diary.core.weather.network.api.entity.WeatherRemoteEntity
import io.github.taetae98coding.diary.core.weather.network.impl.di.WeatherHttpClient
import io.github.taetae98coding.diary.core.weather.network.impl.entity.ForecastResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Factory

@Factory
internal class WeatherRemoteDataSourceImpl(
    @param:WeatherHttpClient
    private val httpClient: HttpClient,
) : WeatherRemoteDataSource {
    override suspend fun getCurrent(
        latitude: Double,
        longitude: Double,
    ): WeatherRemoteEntity {
        return httpClient.get("/data/2.5/weather") {
            parameter("lat", latitude)
            parameter("lon", longitude)
        }.body()
    }

    override suspend fun getForecast(
        latitude: Double,
        longitude: Double,
    ): List<WeatherRemoteEntity> {
        val response = httpClient.get("/data/2.5/forecast") {
            parameter("lat", latitude)
            parameter("lon", longitude)
        }

        return response.body<ForecastResponse>().list
    }
}
