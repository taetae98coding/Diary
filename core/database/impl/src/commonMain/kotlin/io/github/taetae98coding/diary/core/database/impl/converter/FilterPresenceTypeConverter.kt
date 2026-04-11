package io.github.taetae98coding.diary.core.database.impl.converter

import androidx.room3.TypeConverter
import io.github.taetae98coding.diary.core.database.api.entity.FilterPresenceLocalEntity

internal class FilterPresenceTypeConverter {
    @TypeConverter
    fun fromFilterPresence(value: FilterPresenceLocalEntity): Int {
        return value.persistentValue
    }

    @TypeConverter
    fun toFilterPresence(value: Int): FilterPresenceLocalEntity {
        return FilterPresenceLocalEntity.fromPersistentValue(value)
    }
}
