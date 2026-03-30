package io.github.taetae98coding.diary.library.room.common.converter

import androidx.room3.TypeConverter
import kotlinx.datetime.LocalDate

public class LocalDateTypeConverter {
    @TypeConverter
    public fun fromLocalDate(value: LocalDate): String {
        return value.toString()
    }

    @TypeConverter
    public fun toLocalDate(value: String): LocalDate {
        return LocalDate.parse(value)
    }
}
