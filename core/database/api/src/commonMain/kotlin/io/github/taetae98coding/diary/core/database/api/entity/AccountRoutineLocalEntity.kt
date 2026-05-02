package io.github.taetae98coding.diary.core.database.api.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import kotlin.uuid.Uuid

@Entity(
    tableName = "AccountRoutine",
    primaryKeys = ["accountId", "routineId"],
    foreignKeys = [
        ForeignKey(
            entity = RoutineLocalEntity::class,
            parentColumns = ["id"],
            childColumns = ["routineId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["routineId"])],
)
public data class AccountRoutineLocalEntity(
    @ColumnInfo(name = "accountId", defaultValue = "00000000-0000-0000-0000-000000000000")
    val accountId: Uuid,
    @ColumnInfo(name = "routineId", defaultValue = "00000000-0000-0000-0000-000000000000")
    val routineId: Uuid,
)
