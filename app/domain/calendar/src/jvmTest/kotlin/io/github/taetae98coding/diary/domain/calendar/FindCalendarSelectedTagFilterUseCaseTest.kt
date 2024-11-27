package io.github.taetae98coding.diary.domain.calendar

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.calendar.repository.CalendarRepository
import io.github.taetae98coding.diary.domain.calendar.usecase.FindCalendarSelectedTagFilterUseCase
import io.github.taetae98coding.diary.domain.tag.repository.TagRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class FindCalendarSelectedTagFilterUseCaseTest : BehaviorSpec() {
	init {
		val getAccountUseCase = mockk<GetAccountUseCase>()
		val calendarRepository = mockk<CalendarRepository>()
		val tagRepository = mockk<TagRepository>()

		val useCase = FindCalendarSelectedTagFilterUseCase(
			getAccountUseCase = getAccountUseCase,
			calendarRepository = calendarRepository,
			tagRepository = tagRepository,
		)

		Given("has account") {
			val accountUid = "uid"
			val account = mockk<Account>(relaxed = true) {
				every { uid } returns accountUid
			}

			every { getAccountUseCase() } returns flowOf(Result.success(account))

			And("has tag filter") {
				val tagIds = setOf("1", "2", "3")
				every { calendarRepository.findFilter(any()) } returns flowOf(tagIds)

				When("call usecase") {
					every { tagRepository.findByIds(any()) } answers {
						val ids = arg<Set<String>>(0)
						val list = ids.map {
							mockk<Tag> {
								every { id } returns it
							}
						}

						flowOf(list)
					}

					val result = useCase().first()

					Then("result is success") {
						result.shouldBeSuccess()
					}

					Then("do find") {
						verify {
							calendarRepository.findFilter(accountUid)
							tagRepository.findByIds(tagIds)
						}
					}
				}
			}
		}
	}
}
