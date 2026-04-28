package io.github.taetae98coding.diary.data.weather.repository

import io.github.taetae98coding.diary.core.ip.network.api.datasource.IpRemoteDataSource
import io.github.taetae98coding.diary.core.location.api.LocationProvider
import io.github.taetae98coding.diary.core.mapper.toDomain
import io.github.taetae98coding.diary.core.model.weather.Weather
import io.github.taetae98coding.diary.core.weather.network.api.datasource.WeatherRemoteDataSource
import io.github.taetae98coding.diary.core.weather.network.api.entity.WeatherRemoteEntity
import io.github.taetae98coding.diary.domain.weather.repository.WeatherRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
public class WeatherRepositoryImpl(
    @param:Provided
    private val locationProvider: LocationProvider,
    @param:Provided
    private val ipRemoteDataSource: IpRemoteDataSource,
    @param:Provided
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
) : WeatherRepository {
    private val currentWeather = MutableStateFlow(emptyList<Weather>())
    override fun getCurrentWeather(): Flow<List<Weather>> {
        return currentWeather.asStateFlow()
    }

    override suspend fun fetchCurrentWeather() {
        coroutineScope {
            val coordinate = resolveCoordinate()

            listOf(
                async { listOf(weatherRemoteDataSource.getCurrent(coordinate.latitude, coordinate.longitude)) },
                async { weatherRemoteDataSource.getForecast(coordinate.latitude, coordinate.longitude) },
            ).awaitAll()
                .flatten()
                .map(WeatherRemoteEntity::toDomain)
                .also { currentWeather.emit(it) }
        }
    }

    private suspend fun resolveCoordinate(): Coordinate {
        return runCatching { locationProvider.currentLocation.first() }
            .mapCatching { location ->
                if (location == null) {
                    val ipEntity = ipRemoteDataSource.get()
                    Coordinate(ipEntity.latitude, ipEntity.longitude)
                } else {
                    Coordinate(location.latitude, location.longitude)
                }
            }
            .getOrThrow()
    }

    private data class Coordinate(
        val latitude: Double,
        val longitude: Double,
    )
}
