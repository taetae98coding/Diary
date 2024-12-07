package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.domain.backup.usecase.PushMemoBackupQueueUseCase
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import io.github.taetae98coding.diary.domain.memo.repository.MemoTagRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class UnselectMemoTagUseCase internal constructor(
	private val pushMemoBackupQueueUseCase: PushMemoBackupQueueUseCase,
	private val memoRepository: MemoRepository,
	private val memoTagRepository: MemoTagRepository,
) {
	public suspend operator fun invoke(memoId: String?, tagId: String?): Result<Unit> {
		return runCatching {
			if (memoId.isNullOrBlank() || tagId.isNullOrBlank()) return@runCatching

			val memo = memoRepository.getById(memoId).first() ?: return@runCatching

			memoTagRepository.delete(memoId, tagId)
			if (memo.primaryTag == tagId) {
				memoRepository.updatePrimaryTag(memoId, null)
			}
			pushMemoBackupQueueUseCase(memoId)
		}
	}
}
