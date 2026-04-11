package io.github.taetae98coding.diary.library.room.common.converter

import androidx.room3.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json

public class LocalDateListTypeConverter {
    @TypeConverter
    public fun fromLocalDateList(value: List<LocalDate>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    public fun toLocalDateList(value: String): List<LocalDate> {
        return Json.decodeFromString(value)
    }
}
