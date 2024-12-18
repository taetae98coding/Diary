package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.domain.backup.usecase.PushMemoBackupQueueUseCase
import io.github.taetae98coding.diary.domain.memo.repository.MemoBuddyGroupRepository
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class DeleteMemoUseCase internal constructor(
	private val pushMemoBackupQueueUseCase: PushMemoBackupQueueUseCase,
	private val memoRepository: MemoRepository,
	private val memoBuddyGroupRepository: MemoBuddyGroupRepository,
) {
	public suspend operator fun invoke(memoId: String?): Result<Unit> {
		return runCatching {
			if (memoId.isNullOrBlank()) return@runCatching

			memoRepository.updateDelete(memoId, true)
			if (!memoBuddyGroupRepository.isBuddyGroupMemo(memoId).first()) {
				pushMemoBackupQueueUseCase(memoId)
			}
		}
	}
}
