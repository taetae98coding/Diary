package io.github.taetae98coding.diary.domain.weather.usecase

import io.github.taetae98coding.diary.domain.weather.repository.WeatherRepository
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class FetchCurrentWeatherUseCase(
    @param:Provided
    private val weatherRepository: WeatherRepository,
) {
    public suspend operator fun invoke(): Result<Unit> {
        return runCatching {
            weatherRepository.fetchCurrentWeather()
        }
    }
}
