package io.github.taetae98coding.diary.domain.holiday.usecase

import io.github.taetae98coding.diary.domain.holiday.repository.HolidayRepository
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class FetchHolidayUseCase(
    @param:Provided
    private val holidayRepository: HolidayRepository,
) {
    public suspend operator fun invoke(year: Int): Result<Unit> {
        return runCatching {
            holidayRepository.fetch(year)
        }
    }
}
