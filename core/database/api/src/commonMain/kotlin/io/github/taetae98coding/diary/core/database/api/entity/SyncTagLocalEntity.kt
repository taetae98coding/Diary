package io.github.taetae98coding.diary.core.database.api.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.PrimaryKey
import kotlin.uuid.Uuid

@Entity(
    tableName = "SyncTag",
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
public data class SyncTagLocalEntity(
    @PrimaryKey
    @ColumnInfo(name = "tagId", defaultValue = "00000000-0000-0000-0000-000000000000")
    val tagId: Uuid,
    @ColumnInfo(name = "state", defaultValue = "0")
    val state: SyncStateLocalEntity = SyncStateLocalEntity.PENDING,
)
