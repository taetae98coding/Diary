package io.github.taetae98coding.diary.library.room.common.converter

import androidx.room3.TypeConverter
import kotlinx.datetime.LocalDateTime

public class LocalDateTimeTypeConverter {
    @TypeConverter
    public fun fromLocalDateTime(value: LocalDateTime?): String? {
        return value?.toString()
    }

    @TypeConverter
    public fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let(LocalDateTime::parse)
    }
}
