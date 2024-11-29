package io.github.taetae98coding.diary.domain.calendar

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.calendar.repository.CalendarRepository
import io.github.taetae98coding.diary.domain.calendar.usecase.FindCalendarSelectedTagFilterUseCase
import io.github.taetae98coding.diary.domain.tag.repository.TagRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.inspectors.shouldForAll
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class FindCalendarSelectedTagFilterUseCaseTest : BehaviorSpec() {
	private val getAccountUseCase = mockk<GetAccountUseCase>()
	private val calendarRepository = mockk<CalendarRepository>()
	private val tagRepository = mockk<TagRepository>()
	private val useCase = FindCalendarSelectedTagFilterUseCase(
		getAccountUseCase = getAccountUseCase,
		calendarRepository = calendarRepository,
		tagRepository = tagRepository,
	)

	init {
		Given("has account") {
			val accountUid = "uid"
			val account = mockk<Account> {
				every { uid } returns accountUid
			}

			every { getAccountUseCase() } returns flowOf(Result.success(account))

			And("has deleted tag") {
				every { calendarRepository.findFilter(any()) } returns flowOf(setOf())
				every { tagRepository.getByIds(any()) } returns flowOf(
					List(10) { index ->
						mockk {
							every { id } returns index.toString()
							every { isDelete } returns (index % 2 == 0)
						}
					},
				)

				When("call useCase") {

					val result = useCase().first()

					Then("result is success") {
						result.shouldBeSuccess()
					}

					Then("has not deleted tag") {
						result.shouldBeSuccess().shouldForAll { it.isDelete.shouldBeFalse() }
					}

					Then("verity call with uid") {
						verify { calendarRepository.findFilter(accountUid) }
					}
				}
			}
		}
	}
}
