package io.github.taetae98coding.diary.domain.buddy

import io.github.taetae98coding.diary.domain.buddy.repository.BuddyRepository
import io.github.taetae98coding.diary.domain.buddy.usecase.FindBuddyUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class FindBuddyUseCaseTest : BehaviorSpec() {
	private val buddyRepository = mockk<BuddyRepository>(relaxed = true, relaxUnitFun = true)
	private val useCase = FindBuddyUseCase(
		repository = buddyRepository,
	)

	init {
		Given("given email") {
			val email = "email"
			When("call useCase") {
				every { buddyRepository.findBuddyByEmail(any()) } returns flowOf(emptyList())

				val result = useCase(email).first()

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("do find") {
					verify { buddyRepository.findBuddyByEmail(email) }
				}

				clearAllMocks()
			}
		}

		Given("blank email") {
			val email = ""

			When("call useCase") {
				val result = useCase(email).first()

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("do nothing") {
					verify {
						buddyRepository wasNot Called
					}
				}

				clearAllMocks()
			}
		}
	}
}