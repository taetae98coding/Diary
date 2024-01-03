package com.taetae98.diary.data.remote.impl.holiday.entity

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class HolidayEntity(
    @SerialName("dateName")
    val name: String,
    @SerialName("locdate")
    val date: Int,
) {
    fun getLocalDate(): LocalDate {
        val dateString = date.toString()

        return LocalDate(
            year = dateString.substring(0 until 4).toInt(),
            monthNumber = dateString.substring(4 until 6).toInt(),
            dayOfMonth = dateString.substring(6 until 8).toInt(),
        )
    }
}