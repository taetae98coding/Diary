package io.github.taetae98coding.diary.domain.memo

import io.github.taetae98coding.diary.domain.backup.usecase.PushMemoBackupQueueUseCase
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import io.github.taetae98coding.diary.domain.memo.usecase.UpdateMemoPrimaryTagUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.coVerifyOrder
import io.mockk.mockk
import io.mockk.verify

class UpdateMemoPrimaryTagUseCaseTest : BehaviorSpec() {
	private val pushMemoBackupQueueUseCase = mockk<PushMemoBackupQueueUseCase>(relaxed = true, relaxUnitFun = true)
	private val memoRepository = mockk<MemoRepository>(relaxed = true, relaxUnitFun = true)
	private val useCase = UpdateMemoPrimaryTagUseCase(
		pushMemoBackupQueueUseCase = pushMemoBackupQueueUseCase,
		repository = memoRepository,
	)

	init {
		Given("memo id is not null or tag is not null") {
			val memoId = "memoId"
			val tagId = "tagId"

			When("call useCase") {
				val result = useCase(memoId, tagId)

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("1. update 2. backup") {
					coVerifyOrder {
						memoRepository.updatePrimaryTag(memoId, tagId)
						pushMemoBackupQueueUseCase(memoId)
					}
				}

				clearAllMocks()
			}
		}

		Given("memo id is null or tag is null") {
			val memoId = null
			val tagId = null

			When("call useCase") {
				val result = useCase(memoId, tagId)

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("do nothing") {
					verify {
						memoRepository wasNot Called
						pushMemoBackupQueueUseCase wasNot Called
					}
				}

				clearAllMocks()
			}
		}
	}
}
