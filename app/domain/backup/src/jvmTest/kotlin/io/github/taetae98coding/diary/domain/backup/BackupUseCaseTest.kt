package io.github.taetae98coding.diary.domain.backup

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.backup.repository.MemoBackupRepository
import io.github.taetae98coding.diary.domain.backup.repository.TagBackupRepository
import io.github.taetae98coding.diary.domain.backup.usecase.BackupUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf

class BackupUseCaseTest : BehaviorSpec() {
	private val getAccountUseCase = mockk<GetAccountUseCase>()
	private val memoBackupRepository = mockk<MemoBackupRepository>(relaxed = true, relaxUnitFun = true)
	private val tagBackupRepository = mockk<TagBackupRepository>(relaxed = true, relaxUnitFun = true)
	private val useCase = BackupUseCase(
		getAccountUseCase = getAccountUseCase,
		memoBackupRepository = memoBackupRepository,
		tagBackupRepository = tagBackupRepository,
	)

	init {

		Given("account is Guest") {
			every { getAccountUseCase() } returns flowOf(Result.success(mockk<Account.Guest>()))

			When("call useCase") {
				val result = useCase()

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("do nothing") {
					verify {
						tagBackupRepository wasNot Called
						memoBackupRepository wasNot Called
					}
				}
			}

			clearAllMocks()
		}

		Given("account is Member") {
			val accountUid = "uid"
			val account = mockk<Account.Member> {
				every { uid } returns accountUid
			}

			every { getAccountUseCase() } returns flowOf(Result.success(account))

			When("call useCase") {
				val result = useCase()

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("do backup") {
					coVerify {
						tagBackupRepository.backup(accountUid)
						memoBackupRepository.backup(accountUid)
					}
				}

				Then("order 1. tag, 2. memo") {
					coVerifyOrder {
						tagBackupRepository.backup(accountUid)
						memoBackupRepository.backup(accountUid)
					}
				}
			}

			clearAllMocks()
		}
	}
}
