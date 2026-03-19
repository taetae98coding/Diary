package io.github.taetae98coding.diary.domain.memo.repository

import io.github.taetae98coding.diary.core.model.memo.CalendarMemo
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface AccountCalendarMemoRepository {
    public fun get(
        accountId: Uuid,
        year: Int,
    ): Flow<List<CalendarMemo>>
}
