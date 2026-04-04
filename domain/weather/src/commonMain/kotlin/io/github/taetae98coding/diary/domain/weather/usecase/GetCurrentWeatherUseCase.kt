package io.github.taetae98coding.diary.domain.weather.usecase

import io.github.taetae98coding.diary.core.model.weather.Weather
import io.github.taetae98coding.diary.domain.weather.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class GetCurrentWeatherUseCase(
    @param:Provided
    private val weatherRepository: WeatherRepository,
) {
    public operator fun invoke(): Flow<Result<List<Weather>>> {
        return flow { emitAll(weatherRepository.getCurrentWeather()) }
            .mapLatest { Result.success(it) }
            .catch { emit(Result.failure(it)) }
    }
}
