package io.github.taetae98coding.diary.feature.calendar.home

import io.github.taetae98coding.diary.core.model.memo.CalendarMemo
import io.github.taetae98coding.diary.domain.memo.usecase.GetCalendarMemoUseCase
import io.github.taetae98coding.diary.presenter.calendar.api.CalendarMemoStrategy
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class AccountCalendarMemoStrategy(private val getCalendarMemoUseCase: GetCalendarMemoUseCase) : CalendarMemoStrategy {
    override fun get(year: Int): Flow<Result<List<CalendarMemo>>> {
        return getCalendarMemoUseCase(year)
    }
}
