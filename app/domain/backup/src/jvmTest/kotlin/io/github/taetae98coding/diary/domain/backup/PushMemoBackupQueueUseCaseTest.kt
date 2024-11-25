package io.github.taetae98coding.diary.domain.backup

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.backup.repository.MemoBackupRepository
import io.github.taetae98coding.diary.domain.backup.usecase.BackupUseCase
import io.github.taetae98coding.diary.domain.backup.usecase.PushMemoBackupQueueUseCase
import io.kotest.core.concurrency.CoroutineDispatcherFactory
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
class PushMemoBackupQueueUseCaseTest : BehaviorSpec() {
	init {
		val dispatcher = UnconfinedTestDispatcher()
		val scope = CoroutineScope(dispatcher)

		coroutineDispatcherFactory = object : CoroutineDispatcherFactory {
			override suspend fun <T> withDispatcher(
				testCase: TestCase,
				f: suspend () -> T,
			): T = withContext(dispatcher) {
				f()
			}
		}

		val getAccountUseCase = mockk<GetAccountUseCase>()
		val backupUseCase = mockk<BackupUseCase>(relaxed = true, relaxUnitFun = true)
		val memoBackupRepository = mockk<MemoBackupRepository>(relaxed = true, relaxUnitFun = true)
		val useCase = PushMemoBackupQueueUseCase(
			getAccountUseCase = getAccountUseCase,
			backupUseCase = backupUseCase,
			coroutineScope = scope,
			repository = memoBackupRepository,
		)

		Given("memoId is null") {
			val memoId = null

			When("call usecase") {
				val result = useCase(memoId)

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("do nothing") {
					verify {
						getAccountUseCase wasNot Called
						backupUseCase wasNot Called
						memoBackupRepository wasNot Called
					}
				}
			}

			clearAllMocks()
		}

		Given("memoId is not null") {
			val memoId = "memoId"

			And("account is Guest") {
				every { getAccountUseCase() } returns flowOf(Result.success(mockk<Account.Guest>()))

				When("call usecase") {
					val result = useCase(memoId)

					Then("result is success") {
						result.shouldBeSuccess()
					}

					Then("do not backup") {
						verify {
							memoBackupRepository wasNot Called
							backupUseCase wasNot Called
						}
					}
				}

				clearAllMocks()
			}

			And("account is Member") {
				val accountUid = "uid"
				val account = mockk<Account.Member> {
					every { uid } returns accountUid
				}

				every { getAccountUseCase() } returns flowOf(Result.success(account))

				When("call usecase") {
					val result = useCase(memoId)

					Then("result is success") {
						result.shouldBeSuccess()
					}

					Then("do backup") {
						coVerify {
							memoBackupRepository.upsertBackupQueue(accountUid, memoId)
							backupUseCase()
						}
					}
				}

				clearAllMocks()
			}
		}
	}
}
