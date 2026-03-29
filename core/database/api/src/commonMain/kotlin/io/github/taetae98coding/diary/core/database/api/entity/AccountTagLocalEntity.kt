package io.github.taetae98coding.diary.core.database.api.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import kotlin.uuid.Uuid

@Entity(
    tableName = "AccountTag",
    primaryKeys = ["accountId", "tagId"],
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
public data class AccountTagLocalEntity(
    @ColumnInfo(name = "accountId", defaultValue = "00000000-0000-0000-0000-000000000000")
    val accountId: Uuid,
    @ColumnInfo(name = "tagId", defaultValue = "00000000-0000-0000-0000-000000000000")
    val tagId: Uuid,
)
