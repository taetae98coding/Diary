package io.github.taetae98coding.diary.core.filter.database.room.entity

import androidx.room.Entity

@Entity(
	primaryKeys = ["uid", "tagId"],
)
internal data class CalendarFilterEntity(val uid: String, val tagId: String)
