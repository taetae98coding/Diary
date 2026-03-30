package io.github.taetae98coding.diary.core.holiday.network.impl.datasource

import io.github.taetae98coding.diary.core.holiday.network.api.datasource.HolidayRemoteDataSource
import io.github.taetae98coding.diary.core.holiday.network.api.entity.HolidayRemoteEntity
import io.github.taetae98coding.diary.core.holiday.network.impl.di.HolidayHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.core.annotation.Factory

@Factory
internal class HolidayRemoteDataSourceImpl(
    @param:HolidayHttpClient
    private val httpClient: HttpClient,
) : HolidayRemoteDataSource {
    override suspend fun get(year: Int): List<HolidayRemoteEntity> {
        return httpClient.get("holiday/$year.json").body()
    }
}
