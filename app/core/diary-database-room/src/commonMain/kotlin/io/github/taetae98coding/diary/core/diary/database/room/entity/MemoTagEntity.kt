package io.github.taetae98coding.diary.core.diary.database.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
	indices = [
		Index("memoId"), Index("tagId"),
	],
	primaryKeys = ["memoId", "tagId"],
	foreignKeys = [
		ForeignKey(
			entity = MemoEntity::class,
			parentColumns = ["id"],
			childColumns = ["memoId"],
			onDelete = ForeignKey.CASCADE,
			onUpdate = ForeignKey.CASCADE,
		),
		ForeignKey(
			entity = TagEntity::class,
			parentColumns = ["id"],
			childColumns = ["tagId"],
			onDelete = ForeignKey.CASCADE,
			onUpdate = ForeignKey.CASCADE,
		),
	],
)
internal data class MemoTagEntity(
	@ColumnInfo(defaultValue = "")
	val memoId: String,
	@ColumnInfo(defaultValue = "")
	val tagId: String,
)
