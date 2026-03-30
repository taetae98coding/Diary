package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.domain.memo.repository.CalendarMemoFilterTagRepository
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory

@Factory
public class UnselectCalendarMemoFilterTagUseCase(private val calendarMemoFilterTagRepository: CalendarMemoFilterTagRepository) {
    public suspend operator fun invoke(tagId: Uuid): Result<Unit> {
        return runCatching {
            calendarMemoFilterTagRepository.delete(tagId)
        }
    }
}
