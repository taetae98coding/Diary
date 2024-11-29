package io.github.taetae98coding.diary.domain.calendar

import io.github.taetae98coding.diary.domain.calendar.usecase.FindCalendarSelectedTagFilterUseCase
import io.github.taetae98coding.diary.domain.calendar.usecase.HasCalendarFilterUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class HasCalendarFilterUseCaseTest : BehaviorSpec() {
	private val findCalendarSelectedTagFilterUseCase = mockk<FindCalendarSelectedTagFilterUseCase>()
	private val useCase = HasCalendarFilterUseCase(
		findCalendarSelectedTagFilterUseCase = findCalendarSelectedTagFilterUseCase,
	)

	init {
		Given("has selected tag filter") {
			every { findCalendarSelectedTagFilterUseCase() } returns flowOf(
				Result.success(
					List(10) { mockk() },
				),
			)

			When("call useCase") {
				val result = useCase().first()

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("has filter") {
					result.shouldBeSuccess().shouldBeTrue()
				}
			}
		}

		Given("has no selected tag filter") {
			every { findCalendarSelectedTagFilterUseCase() } returns flowOf(Result.success(emptyList()))

			When("call useCase") {
				val result = useCase().first()

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("has no filter") {
					result.shouldBeSuccess().shouldBeFalse()
				}
			}
		}
	}
}
