package io.github.taetae98coding.diary.core.holiday.database.impl.dao

import androidx.room3.Dao
import androidx.room3.Query
import io.github.taetae98coding.diary.core.holiday.database.api.entity.HolidayLocalEntity
import io.github.taetae98coding.diary.library.room.common.dao.RoomDao
import kotlinx.coroutines.flow.Flow

@Dao
internal interface HolidayDao : RoomDao<HolidayLocalEntity> {
    @Query(
        """
        SELECT * FROM Holiday
        WHERE CAST(strftime('%Y', start) AS INTEGER) <= :year
          AND CAST(strftime('%Y', endInclusive) AS INTEGER) >= :year
        """,
    )
    fun get(year: Int): Flow<List<HolidayLocalEntity>>

    @Query(
        """
        DELETE FROM Holiday
        WHERE CAST(strftime('%Y', start) AS INTEGER) <= :year
          AND CAST(strftime('%Y', endInclusive) AS INTEGER) >= :year
        """,
    )
    suspend fun delete(year: Int)
}
