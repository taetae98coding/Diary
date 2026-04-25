package io.github.taetae98coding.diary.domain.routine.usecase

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.core.model.routine.CalendarRoutine
import io.github.taetae98coding.diary.core.model.routine.RRuleDiaryByDay
import io.github.taetae98coding.diary.core.model.routine.Routine
import io.github.taetae98coding.diary.core.model.routine.RoutineRRule
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.routine.repository.AccountCalendarRoutineRepository
import io.github.taetae98coding.diary.library.datetime.toLocalDateRanges
import io.github.taetae98coding.diary.library.datetime.toSundayBasedNumber
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.plus
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class GetCalendarRoutineUseCase(
    private val getAccountUseCase: GetAccountUseCase,
    @param:Provided
    private val accountCalendarRoutineRepository: AccountCalendarRoutineRepository,
) {
    public operator fun invoke(year: Int): Flow<Result<List<CalendarRoutine>>> {
        return flow {
            getAccountUseCase().mapLatest(Result<Account>::getOrThrow)
                .flatMapLatest { accountCalendarRoutineRepository.get(accountId = it.accountId, year = year) }
                .mapLatest { routines -> routines.flatMap { it.expandCalendarRoutines(year) } }
                .also { emitAll(it) }
        }.map {
            Result.success(it)
        }.catch {
            emit(Result.failure(it))
        }
    }
}

private fun Routine.expandCalendarRoutines(year: Int): List<CalendarRoutine> {
    val yearStart = LocalDate(year, 1, 1)
    val yearEnd = LocalDate(year, 12, 31)
    val from = maxOf(yearStart, detail.start ?: yearStart)
    val to = minOf(yearEnd, detail.endInclusive ?: yearEnd)
    if (from > to) return emptyList()

    val activeDates = buildList {
        var cursor = from
        while (cursor <= to) {
            if (isActiveOn(cursor)) add(cursor)
            cursor = cursor.plus(1, DateTimeUnit.DAY)
        }
    }

    return activeDates.toLocalDateRanges().mapIndexed { index, range ->
        CalendarRoutine(
            id = id,
            occurrence = index + 1,
            title = detail.title,
            color = detail.color,
            localDateRange = range,
        )
    }
}

private fun Routine.isActiveOn(date: LocalDate): Boolean {
    detail.start?.let { if (date < it) return false }
    detail.endInclusive?.let { if (date > it) return false }
    if (date in exDates) return false
    if (date in rDates) return true
    return rRules.any { it.matches(date) }
}

private fun RoutineRRule.matches(date: LocalDate): Boolean {
    val diaryByDayApplied = diaryByDay.days.isNotEmpty()
    val byMonthDayApplied = byMonthDay.isNotEmpty()
    if (!diaryByDayApplied && !byMonthDayApplied) return false

    val diaryByDayOk = !diaryByDayApplied || diaryByDay.matches(date)
    val byMonthDayOk = !byMonthDayApplied || byMonthDay.any { it.matchesMonthDay(date) }
    return diaryByDayOk && byMonthDayOk
}

/**
 * Diary의 ordinal 의미는 **일요일 시작 캘린더에서 N번째 주**다.
 * RFC 5545의 BYDAY ordinal("N번째 발생")과 다르다.
 *
 * - `ordinal > 0`: 그 달의 N번째 주 (1주차 = 1일이 속한 주, 일~토)
 * - `ordinal < 0`: 그 달의 끝에서 N번째 주
 *
 * 예) 4/1=수요일인 달에서 1주차는 [전월 일~토(=4/4)]. 따라서 `ordinal=1, days={SUN}`은
 * 그 달에 매칭되는 일요일이 없다(전월 일요일은 그 달의 날짜가 아니므로).
 */
private fun RRuleDiaryByDay.matches(date: LocalDate): Boolean {
    if (date.dayOfWeek !in days) return false
    val ord = ordinal ?: return true
    if (ord == 0) return false

    val firstOfMonth = LocalDate(date.year, date.month, 1)
    val firstOffset = firstOfMonth.dayOfWeek.toSundayBasedNumber()
    val weekOfMonth = (date.day - 1 + firstOffset) / 7 + 1

    return if (ord > 0) {
        weekOfMonth == ord
    } else {
        val lengthOfMonth = lengthOfMonth(date)
        val totalWeeks = (lengthOfMonth - 1 + firstOffset) / 7 + 1
        totalWeeks - weekOfMonth + 1 == -ord
    }
}

private fun Int.matchesMonthDay(date: LocalDate): Boolean {
    return when {
        this > 0 -> date.day == this
        this < 0 -> date.day == lengthOfMonth(date) + this + 1
        else -> false
    }
}

private fun lengthOfMonth(date: LocalDate): Int {
    val first = LocalDate(date.year, date.month, 1)
    val nextMonth = first.plus(1, DateTimeUnit.MONTH)
    return first.daysUntil(nextMonth)
}
