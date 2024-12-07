package io.github.taetae98coding.diary.domain.memo

import io.github.taetae98coding.diary.domain.backup.usecase.PushMemoBackupQueueUseCase
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import io.github.taetae98coding.diary.domain.memo.repository.MemoTagRepository
import io.github.taetae98coding.diary.domain.memo.usecase.UnselectMemoTagUseCase
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

class UnselectMemoTagUseCaseTest : BehaviorSpec() {
	private val pushMemoBackupQueueUseCase = mockk<PushMemoBackupQueueUseCase>(relaxed = true, relaxUnitFun = true)
	private val memoRepository = mockk<MemoRepository>(relaxed = true, relaxUnitFun = true)
	private val memoTagRepository = mockk<MemoTagRepository>(relaxed = true, relaxUnitFun = true)
	private val useCase = UnselectMemoTagUseCase(
		pushMemoBackupQueueUseCase = pushMemoBackupQueueUseCase,
		memoRepository = memoRepository,
		memoTagRepository = memoTagRepository,
	)

	init {
		Given("memo id is not null and tag id is not null") {
			val memoId = "memoId"
			val tagId = "tagId"

			And("memo is null") {
				every { memoRepository.getById(memoId) } returns flowOf(null)

				When("call useCase") {
					val result = useCase(memoId, tagId)

					Then("result is success") {
						result.shouldBeSuccess()
					}

					Then("do nothing") {
						coVerify(exactly = 0) {
							memoRepository.updatePrimaryTag(any(), any())
							memoTagRepository.delete(any(), any())
							pushMemoBackupQueueUseCase(any())
						}
					}
				}

				clearAllMocks()
			}

			And("memo primaryTag and tagId is different") {
				every { memoRepository.getById(memoId) } returns flowOf(
					mockk {
						every { primaryTag } returns "zzz"
					},
				)

				When("call useCase") {
					val result = useCase(memoId, tagId)

					Then("result is success") {
						result.shouldBeSuccess()
					}

					Then("do not updatePrimaryTag") {
						coVerify(exactly = 0) {
							memoRepository.updatePrimaryTag(any(), any())
						}
					}

					Then("delete and backup") {
						coVerifyOrder {
							memoTagRepository.delete(memoId, tagId)
							pushMemoBackupQueueUseCase(memoId)
						}
					}
				}

				clearAllMocks()
			}

			And("memo primaryTag and tagId is same") {
				every { memoRepository.getById(memoId) } returns flowOf(
					mockk {
						every { primaryTag } returns tagId
					},
				)

				When("call useCase") {
					val result = useCase(memoId, tagId)

					Then("result is success") {
						result.shouldBeSuccess()
					}

					Then("1. delete 2. delete primaryTag 3. backup") {
						coVerifyOrder {
							memoTagRepository.delete(memoId, tagId)
							memoRepository.updatePrimaryTag(memoId, null)
							pushMemoBackupQueueUseCase(memoId)
						}
					}
				}

				clearAllMocks()
			}
		}

		Given("memo id is null or tag id is null") {
			val memoId = null
			val tagId = null

			When("call useCase") {
				val result = useCase(memoId, tagId)

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("do nothing") {
					verify {
						memoTagRepository wasNot Called
						memoRepository wasNot Called
						pushMemoBackupQueueUseCase wasNot Called
					}
				}

				clearAllMocks()
			}
		}
	}

	private data class MemoIdAndTagId(
		val memoId: String?,
		val tagId: String?,
	)
}
