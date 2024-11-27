package io.github.taetae98coding.diary.domain.backup

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.backup.repository.TagBackupRepository
import io.github.taetae98coding.diary.domain.backup.usecase.BackupUseCase
import io.github.taetae98coding.diary.domain.backup.usecase.PushTagBackupQueueUseCase
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
class PushTagBackupQueueUseCaseTest : BehaviorSpec() {
	init {
		val dispatcher = UnconfinedTestDispatcher()
		val scope = CoroutineScope(dispatcher)

		coroutineDispatcherFactory = object : CoroutineDispatcherFactory {
			override suspend fun <T> withDispatcher(testCase: TestCase, f: suspend () -> T): T = withContext(dispatcher) {
				f()
			}
		}

		val getAccountUseCase = mockk<GetAccountUseCase>()
		val backupUseCase = mockk<BackupUseCase>(relaxed = true, relaxUnitFun = true)
		val tagBackupRepository = mockk<TagBackupRepository>(relaxed = true, relaxUnitFun = true)
		val useCase = PushTagBackupQueueUseCase(
			getAccountUseCase = getAccountUseCase,
			backupUseCase = backupUseCase,
			coroutineScope = scope,
			repository = tagBackupRepository,
		)

		Given("tagId is null") {
			val tagId = null

			When("call usecase") {
				val result = useCase(tagId)

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("do nothing") {
					verify {
						getAccountUseCase wasNot Called
						backupUseCase wasNot Called
						tagBackupRepository wasNot Called
					}
				}
			}
		}

		Given("tagId is not null") {
			val tagId = "tagId"

			And("account is Guest") {
				every { getAccountUseCase.invoke() } returns flowOf(Result.success(mockk<Account.Guest>()))

				When("call usecase") {
					val result = useCase(tagId)

					Then("result is success") {
						result.shouldBeSuccess()
					}

					Then("do not backup") {
						verify {
							backupUseCase wasNot Called
							tagBackupRepository wasNot Called
						}
					}
				}

				clearAllMocks()
			}

			And("account is Member") {
				val accountUid = "uid"
				val account = mockk<Account.Member> {
					every { this@mockk.uid } returns accountUid
				}

				every { getAccountUseCase.invoke() } returns flowOf(Result.success(account))

				When("call usecase") {
					val result = useCase(tagId)

					Then("result is success") {
						result.shouldBeSuccess()
					}

					Then("do backup") {

						coVerify {
							tagBackupRepository.upsertBackupQueue(accountUid, tagId)
							backupUseCase()
						}
					}
				}

				clearAllMocks()
			}
		}
	}
}
