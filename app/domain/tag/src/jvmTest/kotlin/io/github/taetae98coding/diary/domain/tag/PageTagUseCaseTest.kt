package io.github.taetae98coding.diary.domain.tag

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.tag.repository.TagRepository
import io.github.taetae98coding.diary.domain.tag.usecase.PageTagUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class PageTagUseCaseTest : BehaviorSpec() {
	private val getAccountUseCase = mockk<GetAccountUseCase>()
	private val tagRepository = mockk<TagRepository>(relaxed = true, relaxUnitFun = true)
	private val useCase = PageTagUseCase(
		getAccountUseCase = getAccountUseCase,
		repository = tagRepository,
	)

	init {
		Given("has account") {
			val accountUid = "uid"
			val account = mockk<Account> { every { uid } returns accountUid }

			every { getAccountUseCase() } returns flowOf(Result.success(account))
			every { tagRepository.page(any()) } returns flowOf(emptyList())

			When("call useCase") {
				val result = useCase().first()

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("page by uid") {
					verify { tagRepository.page(accountUid) }
				}
			}
		}
	}
}
