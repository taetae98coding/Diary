package io.github.taetae98coding.diary.library.room.common.converter

import androidx.room3.TypeConverter
import kotlin.uuid.Uuid

public class UuidTypeConverter {
    @TypeConverter
    public fun fromUuid(uuid: Uuid): String {
        return uuid.toString()
    }

    @TypeConverter
    public fun toUuid(value: String): Uuid {
        return Uuid.parse(value)
    }
}
