package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.domain.memo.repository.CalendarMemoFilterTagRepository
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class SelectCalendarMemoFilterTagUseCase(
    @param:Provided
    private val calendarMemoFilterTagRepository: CalendarMemoFilterTagRepository,
) {
    public suspend operator fun invoke(tagId: Uuid): Result<Unit> {
        return runCatching {
            calendarMemoFilterTagRepository.upsert(tagId)
        }
    }
}
