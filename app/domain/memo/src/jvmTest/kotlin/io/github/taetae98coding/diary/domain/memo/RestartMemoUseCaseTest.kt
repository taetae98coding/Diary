package io.github.taetae98coding.diary.domain.memo

import io.github.taetae98coding.diary.domain.backup.usecase.PushMemoBackupQueueUseCase
import io.github.taetae98coding.diary.domain.memo.repository.MemoBuddyGroupRepository
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import io.github.taetae98coding.diary.domain.memo.usecase.RestartMemoUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf

class RestartMemoUseCaseTest : BehaviorSpec() {
	private val pushMemoBackupQueueUseCase = mockk<PushMemoBackupQueueUseCase>(relaxed = true, relaxUnitFun = true)
	private val memoRepository = mockk<MemoRepository>(relaxed = true, relaxUnitFun = true)
	private val memoBuddyGroupRepository = mockk<MemoBuddyGroupRepository> {
		every { isBuddyGroupMemo(any()) } returns flowOf(false)
	}
	private val useCase = RestartMemoUseCase(
		pushMemoBackupQueueUseCase = pushMemoBackupQueueUseCase,
		memoRepository = memoRepository,
		memoBuddyGroupRepository = memoBuddyGroupRepository,
	)

	init {
		Given("memo id is null") {
			val memoId = null

			When("call useCase") {
				val result = useCase(memoId)

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("do nothing") {
					verify {
						memoRepository wasNot Called
						pushMemoBackupQueueUseCase wasNot Called
					}
				}

				clearAllMocks(answers = false)
			}
		}

		Given("memo id is not null") {
			val memoId = "id"

			When("call useCase") {
				val result = useCase(memoId)

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("update finish and push memo backup queue") {
					coVerifyOrder {
						memoRepository.updateFinish(memoId, false)
						pushMemoBackupQueueUseCase(memoId)
					}
				}

				clearAllMocks(answers = false)
			}
		}
	}
}
