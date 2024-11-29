package io.github.taetae98coding.diary.domain.memo

import io.github.taetae98coding.diary.domain.backup.usecase.PushMemoBackupQueueUseCase
import io.github.taetae98coding.diary.domain.memo.repository.MemoTagRepository
import io.github.taetae98coding.diary.domain.memo.usecase.SelectMemoTagUseCase
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.coVerifyOrder
import io.mockk.mockk
import io.mockk.verify

class SelectMemoTagUseCaseTest : FunSpec() {
	private val pushMemoBackupQueueUseCase = mockk<PushMemoBackupQueueUseCase>(relaxed = true, relaxUnitFun = true)
	private val memoTagRepository = mockk<MemoTagRepository>(relaxed = true, relaxUnitFun = true)
	private val useCase = SelectMemoTagUseCase(
		pushMemoBackupQueueUseCase = pushMemoBackupQueueUseCase,
		repository = memoTagRepository,
	)

	init {
		val memoIdList = listOf("memoId", null)
		val tagIdList = listOf("tagId", null)
		val data = buildList {
			memoIdList.forEach { memoId ->
				tagIdList.forEach { tagId ->
					add(MemoIdAndTagId(memoId, tagId))
				}
			}
		}
		context("memoId or tagId is null do nothing, else upsert memoTag and backup") {
			withData(data) { (memoId, tagId) ->
				useCase(memoId, tagId)

				if (memoId.isNullOrBlank() || tagId.isNullOrBlank()) {
					verify {
						memoTagRepository wasNot Called
						pushMemoBackupQueueUseCase wasNot Called
					}
				} else {
					coVerifyOrder {
						memoTagRepository.upsert(memoId, tagId)
						pushMemoBackupQueueUseCase(memoId)
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
