package io.github.taetae98coding.diary.core.holiday.database.impl

import androidx.room3.withWriteTransaction
import io.github.taetae98coding.diary.core.holiday.database.api.HolidayLocalDataSource
import io.github.taetae98coding.diary.core.holiday.database.api.entity.HolidayLocalEntity
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class HolidayLocalDataSourceImpl(private val database: HolidayDatabase) : HolidayLocalDataSource {
    override fun get(year: Int): Flow<List<HolidayLocalEntity>> {
        return database.holidayDao().get(year)
    }

    override suspend fun upsert(
        year: Int,
        entities: List<HolidayLocalEntity>,
    ) {
        database.withWriteTransaction {
            database.holidayDao().delete(year)
            database.holidayDao().upsert(entities)
        }
    }
}
