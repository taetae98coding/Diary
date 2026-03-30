package io.github.taetae98coding.diary.domain.holiday.usecase

import io.github.taetae98coding.diary.domain.holiday.repository.HolidayRepository
import org.koin.core.annotation.Factory

@Factory
public class FetchHolidayUseCase(private val holidayRepository: HolidayRepository) {
    public suspend operator fun invoke(year: Int): Result<Unit> {
        return runCatching {
            holidayRepository.fetch(year)
        }
    }
}
