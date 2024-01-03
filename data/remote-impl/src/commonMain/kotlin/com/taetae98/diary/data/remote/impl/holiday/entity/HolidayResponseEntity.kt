package com.taetae98.diary.data.remote.impl.holiday.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
internal data class HolidayResponseEntity(
    @SerialName("totalCount")
    val count: Int,
    @SerialName("items")
    val items: JsonElement
)
