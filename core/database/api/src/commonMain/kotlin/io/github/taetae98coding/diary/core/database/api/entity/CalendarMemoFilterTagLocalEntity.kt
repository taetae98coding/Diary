package io.github.taetae98coding.diary.core.database.api.entity

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.PrimaryKey
import kotlin.uuid.Uuid

@Entity(
    tableName = "CalendarMemoFilterTag",
    foreignKeys = [
        ForeignKey(
            entity = TagLocalEntity::class,
            parentColumns = ["id"],
            childColumns = ["tagId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
public data class CalendarMemoFilterTagLocalEntity(
    @PrimaryKey
    val tagId: Uuid,
)
