package io.github.taetae98coding.diary.core.diary.database.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
	indices = [
		Index("memoId"), Index("owner"),
	],
	primaryKeys = ["memoId", "owner"],
	foreignKeys = [
		ForeignKey(
			entity = MemoEntity::class,
			parentColumns = ["id"],
			childColumns = ["memoId"],
			onDelete = ForeignKey.CASCADE,
			onUpdate = ForeignKey.CASCADE,
		),
	],
)
internal data class MemoAccountEntity(
	@ColumnInfo(defaultValue = "")
	val memoId: String,
	@ColumnInfo(defaultValue = "")
	val owner: String,
)
