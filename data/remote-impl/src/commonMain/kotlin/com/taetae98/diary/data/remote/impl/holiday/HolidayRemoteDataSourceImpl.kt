package com.taetae98.diary.data.remote.impl.holiday

import com.taetae98.diary.data.dto.holiday.HolidayDto
import com.taetae98.diary.data.remote.api.HolidayRemoteDataSource
import com.taetae98.diary.data.remote.impl.holiday.entity.HolidayEntity
import com.taetae98.diary.data.remote.impl.holiday.entity.HolidayItemEntity
import com.taetae98.diary.data.remote.impl.holiday.entity.HolidayItemsEntity
import com.taetae98.diary.data.remote.impl.holiday.entity.HolidayResponseEntity
import com.taetae98.diary.data.remote.impl.holiday.entity.OpenApiEntity
import com.taetae98.diary.data.remote.impl.holiday.entity.OpenApiResponseEntity
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.datetime.Month
import kotlinx.datetime.number
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class HolidayRemoteDataSourceImpl(
    @Named(HolidayModule.HOLIDAY_CLIENT)
    private val client: HttpClient,
    @Named(HolidayModule.HOLIDAY_JSON)
    private val json: Json,
) : HolidayRemoteDataSource {
    override suspend fun getHoliday(year: Int, month: Month): List<HolidayDto> {
        val response = client.get("getRestDeInfo") {
            parameter("solYear", year)
            parameter(
                "solMonth",
                buildString {
                    if (month.number < 10) append("0")
                    append(month.number)
                }
            )
        }

        return response.body<OpenApiResponseEntity<OpenApiEntity<HolidayResponseEntity>>>()
            .response
            .body
            .getHolidayList()
            .map(HolidayEntity::toDto)
    }

    private fun HolidayResponseEntity.getHolidayList(): List<HolidayEntity> {
        return when (count) {
            0 -> emptyList()
            1 -> listOf(json.decodeFromJsonElement<HolidayItemEntity>(items).item)
            else -> json.decodeFromJsonElement<HolidayItemsEntity>(items).items
        }
    }
}