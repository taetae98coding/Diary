package io.github.taetae98coding.diary.domain.fetch

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.backup.usecase.BackupUseCase
import io.github.taetae98coding.diary.domain.fetch.repository.MemoFetchRepository
import io.github.taetae98coding.diary.domain.fetch.repository.TagFetchRepository
import io.github.taetae98coding.diary.domain.fetch.usecase.FetchUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf

class FetchUseCaseTest : BehaviorSpec() {
	private val getAccountUseCase = mockk<GetAccountUseCase>()
	private val backupUseCase = mockk<BackupUseCase>(relaxed = true, relaxUnitFun = true)
	private val memoFetchRepository = mockk<MemoFetchRepository>(relaxed = true, relaxUnitFun = true)
	private val tagFetchRepository = mockk<TagFetchRepository>(relaxed = true, relaxUnitFun = true)
	private val useCase = FetchUseCase(
		getAccountUseCase = getAccountUseCase,
		backupUseCase = backupUseCase,
		memoFetchRepository = memoFetchRepository,
		tagFetchRepository = tagFetchRepository,
	)

	init {
		Given("account is member") {
			val accountUid = "uid"
			val account = mockk<Account.Member> { every { uid } returns accountUid }

			every { getAccountUseCase() } returns flowOf(Result.success(account))

			When("call useCase") {
				val result = useCase()

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("order 1. backup 2. tag fetch 3. memo fetc") {
					coVerifyOrder {
						backupUseCase()
						tagFetchRepository.fetch(accountUid)
						memoFetchRepository.fetch(accountUid)
					}
				}

				clearAllMocks()
			}
		}

		Given("account is guest") {
			every { getAccountUseCase() } returns flowOf(Result.success(mockk<Account.Guest>()))

			When("call useCase") {
				val result = useCase()

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("do not anything") {
					verify {
						backupUseCase wasNot Called
						tagFetchRepository wasNot Called
						memoFetchRepository wasNot Called
					}
				}

				clearAllMocks()
			}
		}
	}
}
