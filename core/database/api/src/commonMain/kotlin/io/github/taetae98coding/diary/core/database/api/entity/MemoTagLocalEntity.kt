package io.github.taetae98coding.diary.core.database.api.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import kotlin.uuid.Uuid

@Entity(
    tableName = "MemoTag",
    primaryKeys = ["memoId", "tagId"],
    foreignKeys = [
        ForeignKey(
            entity = MemoLocalEntity::class,
            parentColumns = ["id"],
            childColumns = ["memoId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = TagLocalEntity::class,
            parentColumns = ["id"],
            childColumns = ["tagId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["tagId"])],
)
public data class MemoTagLocalEntity(
    @ColumnInfo(name = "memoId", defaultValue = "00000000-0000-0000-0000-000000000000")
    val memoId: Uuid,
    @ColumnInfo(name = "tagId", defaultValue = "00000000-0000-0000-0000-000000000000")
    val tagId: Uuid,
    @ColumnInfo(name = "isMemoTag", defaultValue = "0")
    val isMemoTag: Boolean = false,
    @ColumnInfo(name = "updatedAt", defaultValue = "0")
    val updatedAt: Long = 0L,
)
