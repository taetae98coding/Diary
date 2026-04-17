package io.github.taetae98coding.diary.core.database.impl.converter

import androidx.room3.TypeConverter
import io.github.taetae98coding.diary.core.database.api.entity.RoutineRRuleLocalEntity
import kotlinx.serialization.json.Json

internal class RoutineRRuleListTypeConverter {
    @TypeConverter
    fun fromList(value: List<RoutineRRuleLocalEntity>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toList(value: String): List<RoutineRRuleLocalEntity> {
        if (value.isEmpty()) return emptyList()
        return Json.decodeFromString(value)
    }
}
