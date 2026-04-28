package io.github.taetae98coding.diary.data.weather.repository

import io.github.taetae98coding.diary.core.ip.network.api.datasource.IpRemoteDataSource
import io.github.taetae98coding.diary.core.ip.network.api.entity.IpRemoteEntity
import io.github.taetae98coding.diary.core.location.api.Location
import io.github.taetae98coding.diary.core.location.api.LocationProvider
import io.github.taetae98coding.diary.core.weather.network.api.datasource.WeatherRemoteDataSource
import io.github.taetae98coding.diary.core.weather.network.api.entity.WeahterMainRemoteEntity
import io.github.taetae98coding.diary.core.weather.network.api.entity.WeatherRemoteEntity
import io.github.taetae98coding.diary.core.weather.network.api.entity.WeatherTypeIconRemoteEntity
import io.github.taetae98coding.diary.core.weather.network.api.entity.WeatherTypeRemoteEntity
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlin.time.Clock
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class WeatherRepositoryImplTest : FunSpec() {
    private lateinit var locationProvider: LocationProvider
    private lateinit var ipRemoteDataSource: IpRemoteDataSource
    private lateinit var weatherRemoteDataSource: WeatherRemoteDataSource
    private lateinit var repository: WeatherRepositoryImpl

    private val deviceLocation = Location(latitude = 35.1796, longitude = 129.0756)
    private val ip = IpRemoteEntity(latitude = 37.5665, longitude = 126.9780)
    private val current = WeatherRemoteEntity(
        weather = listOf(WeatherTypeRemoteEntity(icon = WeatherTypeIconRemoteEntity("01d"), description = "clear")),
        main = WeahterMainRemoteEntity(temperature = 20.0, minTemperature = 15.0, maxTemperature = 25.0),
        instant = Clock.System.now(),
    )
    private val forecast = listOf(
        WeatherRemoteEntity(
            weather = listOf(WeatherTypeRemoteEntity(icon = WeatherTypeIconRemoteEntity("02d"), description = "clouds")),
            main = WeahterMainRemoteEntity(temperature = 18.0, minTemperature = 13.0, maxTemperature = 23.0),
            instant = Clock.System.now(),
        ),
    )

    init {
        beforeTest {
            clearAllMocks()
            locationProvider = mockk()
            ipRemoteDataSource = mockk()
            weatherRemoteDataSource = mockk()
            repository = WeatherRepositoryImpl(
                locationProvider,
                ipRemoteDataSource,
                weatherRemoteDataSource,
            )
        }

        test("fetchCurrentWeather - 디바이스 위치가 있을 때 device 좌표로 weather를 호출하고 IP는 호출하지 않는다") {
            every { locationProvider.currentLocation } returns flowOf(deviceLocation)
            coEvery { weatherRemoteDataSource.getCurrent(deviceLocation.latitude, deviceLocation.longitude) } returns current
            coEvery { weatherRemoteDataSource.getForecast(deviceLocation.latitude, deviceLocation.longitude) } returns forecast

            repository.fetchCurrentWeather()

            coVerify(exactly = 0) { ipRemoteDataSource.get() }
            coVerify(exactly = 1) { weatherRemoteDataSource.getCurrent(deviceLocation.latitude, deviceLocation.longitude) }
            coVerify(exactly = 1) { weatherRemoteDataSource.getForecast(deviceLocation.latitude, deviceLocation.longitude) }
        }

        test("fetchCurrentWeather - 디바이스 위치가 null이면 IP fallback으로 weather를 호출한다") {
            every { locationProvider.currentLocation } returns flowOf(null)
            coEvery { ipRemoteDataSource.get() } returns ip
            coEvery { weatherRemoteDataSource.getCurrent(ip.latitude, ip.longitude) } returns current
            coEvery { weatherRemoteDataSource.getForecast(ip.latitude, ip.longitude) } returns forecast

            repository.fetchCurrentWeather()

            coVerify(exactly = 1) { ipRemoteDataSource.get() }
            coVerify(exactly = 1) { weatherRemoteDataSource.getCurrent(ip.latitude, ip.longitude) }
            coVerify(exactly = 1) { weatherRemoteDataSource.getForecast(ip.latitude, ip.longitude) }
        }

        test("fetchCurrentWeather - current와 forecast 결과를 getCurrentWeather Flow에 반영한다") {
            every { locationProvider.currentLocation } returns flowOf(deviceLocation)
            coEvery { weatherRemoteDataSource.getCurrent(deviceLocation.latitude, deviceLocation.longitude) } returns current
            coEvery { weatherRemoteDataSource.getForecast(deviceLocation.latitude, deviceLocation.longitude) } returns forecast

            repository.fetchCurrentWeather()

            val result = repository.getCurrentWeather().first()
            result shouldHaveSize 1 + forecast.size
        }

        test("getCurrentWeather - fetch 전에는 빈 리스트를 반환한다") {
            val result = repository.getCurrentWeather().first()
            result.shouldBeEmpty()
        }
    }
}
