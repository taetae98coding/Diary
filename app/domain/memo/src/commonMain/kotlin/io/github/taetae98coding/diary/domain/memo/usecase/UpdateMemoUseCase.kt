package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.domain.backup.usecase.PushMemoBackupQueueUseCase
import io.github.taetae98coding.diary.domain.memo.repository.MemoBuddyGroupRepository
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class UpdateMemoUseCase internal constructor(
	private val pushMemoBackupQueueUseCase: PushMemoBackupQueueUseCase,
	private val memoRepository: MemoRepository,
	private val memoBuddyGroupRepository: MemoBuddyGroupRepository,
) {
	public suspend operator fun invoke(memoId: String?, detail: MemoDetail): Result<Unit> {
		return runCatching {
			if (memoId.isNullOrBlank()) return@runCatching

			val memo = memoRepository.getById(memoId).first() ?: return@runCatching
			val validDetail = detail.copy(title = detail.title.ifBlank { memo.detail.title })

			if (memo.detail == validDetail) return@runCatching

			memoRepository.update(memoId, validDetail)
			if (!memoBuddyGroupRepository.isBuddyGroupMemo(memoId).first()) {
				pushMemoBackupQueueUseCase(memoId)
			}
		}
	}
}
