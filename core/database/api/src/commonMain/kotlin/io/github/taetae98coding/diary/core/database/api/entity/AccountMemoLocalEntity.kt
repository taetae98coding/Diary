package io.github.taetae98coding.diary.core.database.api.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import kotlin.uuid.Uuid

@Entity(
    primaryKeys = ["accountId", "memoId"],
    foreignKeys = [
        ForeignKey(
            entity = MemoLocalEntity::class,
            parentColumns = ["id"],
            childColumns = ["memoId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
public data class AccountMemoLocalEntity(
    @ColumnInfo(defaultValue = "00000000-0000-0000-0000-000000000000")
    val accountId: Uuid,
    @ColumnInfo(defaultValue = "00000000-0000-0000-0000-000000000000")
    val memoId: Uuid,
)
