package io.github.taetae98coding.diary.domain.holiday

import io.github.taetae98coding.diary.domain.holiday.repository.HolidayRepository
import io.github.taetae98coding.diary.domain.holiday.usecase.FindHolidayUseCase
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Month

class FindHolidayUseCaseTest : FunSpec() {
	private val holidayRepository = mockk<HolidayRepository> {
		every { findHoliday(any(), any()) } returns flowOf(emptyList())
	}
	private val useCase = FindHolidayUseCase(
		repository = holidayRepository,
	)

	init {
		test("findHoliday") {
			val year = 2000
			val month = Month.JANUARY
			val result = useCase(year, month).first()

			result.shouldBeSuccess()
			verify { holidayRepository.findHoliday(year, month) }
		}
	}
}
