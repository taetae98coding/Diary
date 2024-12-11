package io.github.taetae98coding.diary.domain.memo

import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.domain.backup.usecase.PushMemoBackupQueueUseCase
import io.github.taetae98coding.diary.domain.memo.repository.MemoBuddyGroupRepository
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import io.github.taetae98coding.diary.domain.memo.usecase.UpdateMemoUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class UpdateMemoUseCaseTest : BehaviorSpec() {
	private val pushMemoBackupQueueUseCase = mockk<PushMemoBackupQueueUseCase>(relaxed = true, relaxUnitFun = true)
	private val memoRepository = mockk<MemoRepository>(relaxed = true, relaxUnitFun = true)
	private val memoBuddyGroupRepository = mockk<MemoBuddyGroupRepository> {
		every { isBuddyGroupMemo(any()) } returns flowOf(false)
	}
	private val useCase = UpdateMemoUseCase(
		pushMemoBackupQueueUseCase = pushMemoBackupQueueUseCase,
		memoRepository = memoRepository,
		memoBuddyGroupRepository = memoBuddyGroupRepository,
	)

	init {
		Given("title not blank detail") {
			val detail = MemoDetail(
				title = "title",
				description = "description",
				start = null,
				endInclusive = null,
				color = 0,
			)

			And("origin detail and new detail different") {
				every { memoRepository.getById(any()) } returns flowOf(mockk(relaxed = true, relaxUnitFun = true))

				When("call useCase") {
					val result = useCase("memoId", detail)

					Then("result is success") {
						result.shouldBeSuccess()
					}

					Then("do update") {
						coVerify {
							memoRepository.update(any(), any())
							pushMemoBackupQueueUseCase(any())
						}
					}

					clearAllMocks(answers = false)
				}
			}

			And("origin detail and new detail same") {
				every { memoRepository.getById(any()) } returns flowOf(
					mockk {
						every { this@mockk.detail } returns detail
					},
				)

				When("call useCase") {
					val result = useCase("memoId", detail)

					Then("result is success") {
						result.shouldBeSuccess()
					}

					Then("do not update") {
						coVerify(exactly = 0) {
							memoRepository.update(any(), any())
							pushMemoBackupQueueUseCase(any())
						}
					}

					clearAllMocks(answers = false)
				}
			}
		}

		Given("title blank detail") {
			val detail = MemoDetail(
				title = "",
				description = "description",
				start = null,
				endInclusive = null,
				color = 0,
			)

			When("call useCase") {
				every { memoRepository.getById(any()) } returns flowOf(
					mockk {
						every { this@mockk.detail } returns mockk {
							every { title } returns "title"
						}
					},
				)

				val result = useCase("memoId", detail)

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("detail title set to origin title") {
					coVerify { memoRepository.update(any(), detail.copy(title = "title")) }
				}

				clearAllMocks(answers = false)
			}
		}
	}
}
