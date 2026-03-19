package io.github.taetae98coding.diary.core.database.impl.converter

import androidx.room3.TypeConverter
import io.github.taetae98coding.diary.core.database.api.entity.SyncStateLocalEntity

internal class SyncStateTypeConverter {
    @TypeConverter
    fun fromSyncState(value: SyncStateLocalEntity): Int {
        return value.persistentValue
    }

    @TypeConverter
    fun toSyncState(value: Int): SyncStateLocalEntity {
        return SyncStateLocalEntity.fromPersistentValue(value)
    }
}
