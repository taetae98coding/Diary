package io.github.taetae98coding.diary.data.routine.repository

import io.github.taetae98coding.diary.core.database.api.datasource.AccountCalendarRoutineLocalDataSource
import io.github.taetae98coding.diary.core.mapper.toDomain
import io.github.taetae98coding.diary.core.model.routine.Routine
import io.github.taetae98coding.diary.domain.routine.repository.AccountCalendarRoutineRepository
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class AccountCalendarRoutineRepositoryImpl(
    @param:Provided
    private val accountCalendarRoutineLocalDataSource: AccountCalendarRoutineLocalDataSource,
) : AccountCalendarRoutineRepository {
    override fun get(
        accountId: Uuid,
        year: Int,
    ): Flow<List<Routine>> {
        return accountCalendarRoutineLocalDataSource.get(accountId, year)
            .map { entities -> entities.map { it.toDomain() } }
    }
}
