package io.github.taetae98coding.diary.core.backup.database.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class MemoBackupEntity(
	@PrimaryKey
	val memoId: String,
	val uid: String,
)
