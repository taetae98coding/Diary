package io.github.taetae98coding.diary.core.holiday.service

import io.github.taetae98coding.diary.core.holiday.service.entity.ApiResultEntity
import io.github.taetae98coding.diary.core.holiday.service.entity.BodyEntity
import io.github.taetae98coding.diary.core.holiday.service.entity.HolidayEntity
import io.github.taetae98coding.diary.core.holiday.service.entity.HolidayItemEntity
import io.github.taetae98coding.diary.core.holiday.service.entity.HolidayItemsEntity
import io.github.taetae98coding.diary.core.holiday.service.mapper.toHoliday
import io.github.taetae98coding.diary.core.model.holiday.Holiday
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.datetime.Month
import kotlinx.datetime.number
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

public class HolidayService internal constructor(
	private val client: HttpClient,
	private val json: Json,
) {
	public suspend fun findHoliday(year: Int, month: Month): List<Holiday> {
		val response =
			client.get("getRestDeInfo") {
				parameter("solYear", year)
				parameter("solMonth", month.number.toString().padStart(2, '0'))
			}

		return response
			.body<ApiResultEntity>()
			.parseHolidayEntity(json)
			.map(HolidayEntity::toHoliday)
	}

	private fun ApiResultEntity.parseHolidayEntity(json: Json): List<HolidayEntity> = response.body.parseHolidayEntity(json)

	private fun BodyEntity.parseHolidayEntity(json: Json): List<HolidayEntity> =
		when (count) {
			0 -> emptyList()
			1 -> listOf(json.decodeFromJsonElement<HolidayItemEntity>(items).item)
			else -> json.decodeFromJsonElement<HolidayItemsEntity>(items).items
		}
}
