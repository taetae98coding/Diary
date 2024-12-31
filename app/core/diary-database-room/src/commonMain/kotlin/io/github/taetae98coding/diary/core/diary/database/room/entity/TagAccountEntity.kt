package io.github.taetae98coding.diary.core.diary.database.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
	indices = [
		Index("tagId"), Index("owner"),
	],
	primaryKeys = ["tagId", "owner"],
	foreignKeys = [
		ForeignKey(
			entity = TagEntity::class,
			parentColumns = ["id"],
			childColumns = ["tagId"],
			onDelete = ForeignKey.CASCADE,
			onUpdate = ForeignKey.CASCADE,
		),
	],
)
internal data class TagAccountEntity(
	@ColumnInfo(defaultValue = "")
	val tagId: String,
	@ColumnInfo(defaultValue = "")
	val owner: String,
)
