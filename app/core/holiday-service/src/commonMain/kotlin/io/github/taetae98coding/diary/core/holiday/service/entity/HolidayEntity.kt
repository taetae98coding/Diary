package io.github.taetae98coding.diary.core.holiday.service.entity

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class HolidayEntity(
    @SerialName("dateName")
    val name: String,
    @SerialName("locdate")
    private val date: Int,
) {
    val localDate: LocalDate
        get() {
            val dateString = date.toString()

            return LocalDate(
                year = dateString.substring(0 until 4).toInt(),
                monthNumber = dateString.substring(4 until 6).toInt(),
                dayOfMonth = dateString.substring(6 until 8).toInt(),
            )
        }
}
