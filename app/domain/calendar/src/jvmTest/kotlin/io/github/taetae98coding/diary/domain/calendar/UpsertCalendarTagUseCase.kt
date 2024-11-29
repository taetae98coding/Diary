package io.github.taetae98coding.diary.domain.calendar

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.calendar.repository.CalendarRepository
import io.github.taetae98coding.diary.domain.calendar.usecase.UpsertCalendarTagUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class UpsertCalendarTagUseCase : BehaviorSpec() {
	private val getAccountUseCase = mockk<GetAccountUseCase>()
	private val calendarRepository = mockk<CalendarRepository>(relaxed = true, relaxUnitFun = true)
	private val useCase = UpsertCalendarTagUseCase(
		getAccountUseCase = getAccountUseCase,
		repository = calendarRepository,
	)

	init {
		Given("has account") {
			val accountUid = "uid"
			val account = mockk<Account> { every { uid } returns accountUid }

			every { getAccountUseCase() } returns flowOf(Result.success(account))

			When("call useCase") {
				val tagId = "tagId"
				val result = useCase(tagId)

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("do upsert") {
					coVerify {
						calendarRepository.upsert(accountUid, tagId)
					}
				}
			}
		}
	}
}
