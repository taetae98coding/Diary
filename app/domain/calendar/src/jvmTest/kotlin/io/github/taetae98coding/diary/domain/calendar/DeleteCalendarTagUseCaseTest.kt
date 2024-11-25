package io.github.taetae98coding.diary.domain.calendar

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.calendar.repository.CalendarRepository
import io.github.taetae98coding.diary.domain.calendar.usecase.DeleteCalendarTagUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf

class DeleteCalendarTagUseCaseTest : BehaviorSpec() {
	init {
		val getAccountUseCase = mockk<GetAccountUseCase>()
		val calendarRepository = mockk<CalendarRepository>(relaxed = true, relaxUnitFun = true)

		val useCase = DeleteCalendarTagUseCase(
			getAccountUseCase = getAccountUseCase,
			repository = calendarRepository,
		)

		Given("has account") {
			val accountUid = "uid"
			val account = mockk<Account> {
				every { uid } returns accountUid
			}

			every { getAccountUseCase() } returns flowOf(Result.success(account))

			And("tagId is null") {
				val tagId = null

				When("call usecase") {
					val result = useCase(tagId)

					Then("result is success") {
						result.shouldBeSuccess()
					}

					Then("do nothing") {
						verify {
							getAccountUseCase wasNot Called
							calendarRepository wasNot Called
						}
					}
				}

				clearAllMocks(answers = false)
			}

			And("tagId is not null") {
				val tagId = "tagId"

				When("call usecase") {
					val result = useCase(tagId)

					Then("result is success") {
						result.shouldBeSuccess()
					}

					Then("do delete") {

						coVerify {
							calendarRepository.delete(accountUid, tagId)
						}
					}
				}

				clearAllMocks(answers = false)
			}
		}
	}
}
