package io.github.taetae98coding.diary.core.holiday.database.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class HolidayEntityDao {
	@Query(
		"""
		SELECT *
		FROM HolidayEntity
		WHERE (CAST(STRFTIME('%Y', start) AS INTEGER) = :year OR CAST(STRFTIME('%Y', endInclusive) AS INTEGER) = :year)
		AND (CAST(STRFTIME('%m', start) AS INTEGER) = :month OR CAST(STRFTIME('%m', endInclusive) AS INTEGER) = :month)
	""",
	)
	abstract fun findHoliday(year: Int, month: Int): Flow<List<HolidayEntity>>

	@Insert(HolidayEntity::class)
	protected abstract suspend fun insert(holiday: List<HolidayEntity>)

	@Query(
		"""
		DELETE FROM HolidayEntity
		WHERE (CAST(STRFTIME('%Y', start) AS INTEGER) = :year OR CAST(STRFTIME('%Y', endInclusive) AS INTEGER) = :year)
		AND (CAST(STRFTIME('%m', start) AS INTEGER) = :month OR CAST(STRFTIME('%m', endInclusive) AS INTEGER) = :month)
	""",
	)
	protected abstract suspend fun delete(year: Int, month: Int)

	@Transaction
	open suspend fun upsert(year: Int, month: Int, holiday: List<HolidayEntity>) {
		delete(year, month)
		insert(holiday)
	}
}
