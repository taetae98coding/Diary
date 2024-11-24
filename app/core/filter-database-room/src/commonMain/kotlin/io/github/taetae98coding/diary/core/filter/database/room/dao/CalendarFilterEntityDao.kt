package io.github.taetae98coding.diary.core.filter.database.room.dao

import androidx.room.Dao
import androidx.room.Query
import io.github.taetae98coding.diary.core.filter.database.room.entity.CalendarFilterEntity
import io.github.taetae98coding.diary.library.room.dao.EntityDao
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class CalendarFilterEntityDao : EntityDao<CalendarFilterEntity>() {
	@Query("SELECT tagId FROM CalendarFilterEntity WHERE uid = :uid")
	abstract fun findAll(uid: String): Flow<List<String>>
}
