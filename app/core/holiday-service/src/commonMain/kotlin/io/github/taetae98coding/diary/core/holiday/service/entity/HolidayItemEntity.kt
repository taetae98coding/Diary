package io.github.taetae98coding.diary.core.holiday.service.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class HolidayItemEntity(
	@SerialName("item")
	val item: HolidayEntity,
)
