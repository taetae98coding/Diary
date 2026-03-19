package io.github.taetae98coding.diary.core.database.api.entity

import androidx.room3.ColumnInfo
import androidx.room3.Embedded
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import kotlin.uuid.Uuid

@Entity(tableName = "Memo")
public data class MemoLocalEntity(
    @PrimaryKey
    @ColumnInfo(name = "id", defaultValue = "00000000-0000-0000-0000-000000000000")
    val id: Uuid,
    @Embedded
    val detail: MemoDetailLocalEntity,
    @ColumnInfo(name = "isFinished", defaultValue = "0")
    val isFinished: Boolean = false,
    @ColumnInfo(name = "isDeleted", defaultValue = "0")
    val isDeleted: Boolean = false,
    @ColumnInfo(name = "updatedAt", defaultValue = "0")
    val updatedAt: Long = 0L,
    @ColumnInfo(name = "createdAt", defaultValue = "0")
    val createdAt: Long = 0L,
)
