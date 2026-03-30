package io.github.taetae98coding.diary.presenter.calendar.api

import io.github.taetae98coding.diary.core.model.memo.CalendarMemo
import kotlinx.coroutines.flow.Flow

public interface CalendarMemoStrategy {
    public fun get(year: Int): Flow<Result<List<CalendarMemo>>>
}
