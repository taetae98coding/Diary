package io.github.taetae98coding.diary.domain.buddy

import io.github.taetae98coding.diary.common.exception.buddy.BuddyGroupTitleBlankException
import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.core.model.buddy.BuddyGroup
import io.github.taetae98coding.diary.core.model.buddy.BuddyGroupDetail
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.buddy.repository.BuddyRepository
import io.github.taetae98coding.diary.domain.buddy.usecase.AddBuddyGroupUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf

class AddBuddyGroupUseCaseTest : BehaviorSpec() {
	private val getAccountUseCase = mockk<GetAccountUseCase>()
	private val buddyRepository = mockk<BuddyRepository>(relaxed = true, relaxUnitFun = true)
	private val useCase = AddBuddyGroupUseCase(
		getAccountUseCase = getAccountUseCase,
		repository = buddyRepository,
	)

	init {
		Given("title is not blank") {
			val detail = mockk<BuddyGroupDetail> {
				every { title } returns "title"
			}
			val buddyIds = setOf("id1")

			And("account is guest") {
				val account = mockk<Account.Guest>()

				every { getAccountUseCase() } returns flowOf(Result.success(account))

				When("call useCase") {
					val result = useCase(detail, buddyIds)

					Then("throw IllegalStateException") {
						result.shouldBeFailure<IllegalStateException>()
					}

					Then("do nothing") {
						verify {
							buddyRepository wasNot Called
						}
					}

					clearAllMocks(answers = false)
				}
			}

			And("account is member") {
				val accountUid = "uid"
				val account = mockk<Account.Member> { every { uid } returns accountUid }

				coEvery { buddyRepository.getNextBuddyGroupId() } returns "buddyGroupId"
				every { getAccountUseCase() } returns flowOf(Result.success(account))

				When("call useCase") {
					val result = useCase(detail, buddyIds)

					Then("result is success") {
						result.shouldBeSuccess()
					}

					Then("do add") {
						val buddyGroup = BuddyGroup(
							id = "buddyGroupId",
							detail = detail,
						)
						val validBuddyIds = buildSet {
							addAll(buddyIds)
							add(accountUid)
						}

						coVerify {
							buddyRepository.upsert(buddyGroup, validBuddyIds)
						}
					}

					clearAllMocks(answers = false)
				}
			}
		}

		Given("title is blank") {
			val detail = mockk<BuddyGroupDetail> {
				every { title } returns ""
			}

			When("call useCase") {
				val result = useCase(detail, emptySet())

				Then("throw BuddyGroupTitleBlankException") {
					result.shouldBeFailure<BuddyGroupTitleBlankException>()
				}

				Then("do nothing") {
					verify {
						buddyRepository wasNot Called
					}
				}
			}
		}
	}
}
