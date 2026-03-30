package io.github.taetae98coding.diary.presenter.calendar.api

import kotlinx.coroutines.flow.Flow

public interface CalendarMemoFilterStrategy {
    public fun hasFilter(): Flow<Result<Boolean>>
}
