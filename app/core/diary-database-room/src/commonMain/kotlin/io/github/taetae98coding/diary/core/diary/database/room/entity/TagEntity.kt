package io.github.taetae98coding.diary.core.diary.database.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity
internal data class TagEntity(
	@PrimaryKey
	@ColumnInfo(defaultValue = "")
	val id: String,
	@ColumnInfo(defaultValue = "")
	val title: String,
	@ColumnInfo(defaultValue = "")
	val description: String,
	@ColumnInfo(defaultValue = "-16777216")
	val color: Int,
	@ColumnInfo(defaultValue = "0")
	val isFinish: Boolean,
	@ColumnInfo(defaultValue = "0")
	val isDelete: Boolean,
	@ColumnInfo(defaultValue = "null")
	val owner: String?,
	@ColumnInfo(defaultValue = "0")
	val updateAt: Instant,
	@ColumnInfo(defaultValue = "null")
	val serverUpdateAt: Instant?,
)
