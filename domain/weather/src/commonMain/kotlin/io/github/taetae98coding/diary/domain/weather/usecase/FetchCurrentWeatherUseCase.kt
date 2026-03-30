package io.github.taetae98coding.diary.domain.weather.usecase

import io.github.taetae98coding.diary.domain.weather.repository.WeatherRepository
import org.koin.core.annotation.Factory

@Factory
public class FetchCurrentWeatherUseCase(private val weatherRepository: WeatherRepository) {
    public suspend operator fun invoke(): Result<Unit> {
        return runCatching {
            weatherRepository.fetchCurrentWeather()
        }
    }
}
