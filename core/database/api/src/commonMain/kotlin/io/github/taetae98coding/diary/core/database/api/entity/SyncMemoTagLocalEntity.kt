package io.github.taetae98coding.diary.core.database.api.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import kotlin.uuid.Uuid

@Entity(
    tableName = "SyncMemoTag",
    primaryKeys = ["memoId", "tagId"],
    foreignKeys = [
        ForeignKey(
            entity = MemoTagLocalEntity::class,
            parentColumns = ["memoId", "tagId"],
            childColumns = ["memoId", "tagId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
public data class SyncMemoTagLocalEntity(
    @ColumnInfo(name = "memoId", defaultValue = "00000000-0000-0000-0000-000000000000")
    val memoId: Uuid,
    @ColumnInfo(name = "tagId", defaultValue = "00000000-0000-0000-0000-000000000000")
    val tagId: Uuid,
    @ColumnInfo(name = "state", defaultValue = "0")
    val state: SyncStateLocalEntity = SyncStateLocalEntity.PENDING,
)
