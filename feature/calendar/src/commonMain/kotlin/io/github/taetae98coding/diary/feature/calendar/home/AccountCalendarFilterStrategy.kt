package io.github.taetae98coding.diary.feature.calendar.home

import io.github.taetae98coding.diary.domain.memo.usecase.HasCalendarMemoFilterUseCase
import io.github.taetae98coding.diary.presenter.calendar.api.CalendarMemoFilterStrategy
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class AccountCalendarFilterStrategy(private val hasCalendarMemoFilterUseCase: HasCalendarMemoFilterUseCase) : CalendarMemoFilterStrategy {

    override fun hasFilter(): Flow<Result<Boolean>> {
        return hasCalendarMemoFilterUseCase()
    }
}
