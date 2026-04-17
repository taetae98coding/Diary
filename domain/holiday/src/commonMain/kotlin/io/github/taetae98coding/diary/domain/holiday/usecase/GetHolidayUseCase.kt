package io.github.taetae98coding.diary.domain.holiday.usecase

import io.github.taetae98coding.diary.core.model.holiday.Holiday
import io.github.taetae98coding.diary.domain.holiday.repository.HolidayFilterRepository
import io.github.taetae98coding.diary.domain.holiday.repository.HolidayRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class GetHolidayUseCase(
    @param:Provided
    private val holidayRepository: HolidayRepository,
    @param:Provided
    private val holidayFilterRepository: HolidayFilterRepository,
) {
    public operator fun invoke(year: Int): Flow<Result<List<Holiday>>> {
        return flow {
            combine(
                holidayRepository.get(year),
                holidayFilterRepository.get(),
            ) { holidays, filter ->
                if (filter.isEmpty()) {
                    holidays
                } else {
                    holidays.filter { it.isHoliday || it.name in filter }
                }
            }.also {
                emitAll(it)
            }
        }.map {
            Result.success(it)
        }.catch {
            emit(Result.failure(it))
        }
    }
}
