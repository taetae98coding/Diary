package io.github.taetae98coding.diary.core.backup.database.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class TagBackupEntity(
	@PrimaryKey
	val tagId: String,
	val uid: String,
)
