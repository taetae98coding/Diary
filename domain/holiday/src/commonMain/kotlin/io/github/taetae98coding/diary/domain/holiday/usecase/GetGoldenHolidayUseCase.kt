package io.github.taetae98coding.diary.domain.holiday.usecase

import io.github.taetae98coding.diary.core.model.holiday.GoldenHoliday
import io.github.taetae98coding.diary.core.model.holiday.Holiday
import io.github.taetae98coding.diary.domain.holiday.repository.HolidayRepository
import io.github.taetae98coding.diary.library.datetime.isWeekend
import io.github.taetae98coding.diary.library.datetime.overlaps
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.koin.core.annotation.Factory

@Factory
public class GetGoldenHolidayUseCase internal constructor(private val holidayRepository: HolidayRepository) {
    public operator fun invoke(
        year: Int,
        annualLeave: Int,
    ): Flow<Result<List<GoldenHoliday>>> {
        return flow {
            holidayRepository.get(year).mapLatest { holidayList ->
                (LocalDate(year, 1, 1)..LocalDate(year, 12, 31)).asSequence()
                    .map { localDate -> calculateGoldenHoliday(localDate, holidayList, annualLeave) }
                    .filter { it.totalDays >= 3 + annualLeave }
                    .filter { it.holiday.isNotEmpty() }
                    .filter { it.yearMonth.year == year }
                    .distinctBy { it.holiday }
                    .toList()
            }.also {
                emitAll(it)
            }
        }.mapLatest {
            Result.success(it)
        }.catch {
            emit(Result.failure(it))
        }
    }

    private fun calculateGoldenHoliday(
        localDate: LocalDate,
        holidayList: List<Holiday>,
        annualLeave: Int,
    ): GoldenHoliday {
        val annualLeaveList = mutableListOf<LocalDate>()

        var startExclusive = localDate
        while (true) {
            if (isDayOff(startExclusive, holidayList, annualLeaveList)) {
                startExclusive = startExclusive.minus(1, DateTimeUnit.DAY)
            } else if (annualLeaveList.size < annualLeave) {
                annualLeaveList.add(startExclusive)
                startExclusive = startExclusive.minus(1, DateTimeUnit.DAY)
            } else {
                break
            }
        }

        var endExclusive = localDate
        while (true) {
            if (isDayOff(endExclusive, holidayList, annualLeaveList)) {
                endExclusive = endExclusive.plus(1, DateTimeUnit.DAY)
            } else if (annualLeaveList.size < annualLeave) {
                annualLeaveList.add(endExclusive)
                endExclusive = endExclusive.plus(1, DateTimeUnit.DAY)
            } else {
                break
            }
        }

        val dateRange = (startExclusive.plus(1, DateTimeUnit.DAY))..(endExclusive.minus(1, DateTimeUnit.DAY))

        return GoldenHoliday(
            localDateRange = dateRange,
            holiday = holidayList.filter { it.isHoliday && it.localDateRange overlaps dateRange },
            annualLeave = annualLeaveList.asSequence()
                .sorted()
                .distinct()
                .withIndex()
                .groupBy { it.value.toEpochDays() - it.index }
                .values
                .map { it.first().value..it.last().value },
        )
    }

    private fun isDayOff(
        localDate: LocalDate,
        holidayList: List<Holiday>,
        annualLeaveList: List<LocalDate>,
    ): Boolean {
        return localDate.dayOfWeek.isWeekend() || holidayList.any { it.isHoliday && localDate in it.localDateRange } || localDate in annualLeaveList
    }
}
