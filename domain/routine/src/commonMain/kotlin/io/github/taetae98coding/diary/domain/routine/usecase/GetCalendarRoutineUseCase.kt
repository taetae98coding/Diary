package io.github.taetae98coding.diary.domain.routine.usecase

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.core.model.routine.CalendarRoutine
import io.github.taetae98coding.diary.core.model.routine.Routine
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.routine.repository.AccountRoutineRepository
import io.github.taetae98coding.diary.library.datetime.toLocalDateRanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class GetCalendarRoutineUseCase(
    private val getAccountUseCase: GetAccountUseCase,
    @param:Provided
    private val accountRoutineRepository: AccountRoutineRepository,
) {
    public operator fun invoke(year: Int): Flow<Result<List<CalendarRoutine>>> {
        return flow {
            getAccountUseCase().mapLatest(Result<Account>::getOrThrow)
                .flatMapLatest { accountRoutineRepository.getByYear(accountId = it.accountId, year = year) }
                .mapLatest { routines -> routines.flatMap { routine -> mapCalendarRoutine(year, routine) } }
                .also { emitAll(it) }
        }.map {
            Result.success(it)
        }.catch {
            emit(Result.failure(it))
        }
    }

    private fun mapCalendarRoutine(
        year: Int,
        routine: Routine,
    ): List<CalendarRoutine> {
        return (LocalDate(year - 1, Month.DECEMBER, 1)..LocalDate(year + 1, Month.JANUARY, 31))
            .filter(routine::isActive)
            .toLocalDateRanges()
            .filter { it.start.year <= year && year <= it.endInclusive.year }
            .mapIndexed { index, localDateRange ->
                CalendarRoutine(
                    id = routine.id,
                    yearIndex = index,
                    title = routine.detail.title,
                    localDateRange = localDateRange,
                    color = routine.detail.color,
                )
            }
    }
}
