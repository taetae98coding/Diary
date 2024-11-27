package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.domain.backup.usecase.PushMemoBackupQueueUseCase
import io.github.taetae98coding.diary.domain.memo.repository.MemoTagRepository
import org.koin.core.annotation.Factory

@Factory
public class SelectMemoTagUseCase internal constructor(
	private val pushMemoBackupQueueUseCase: PushMemoBackupQueueUseCase,
	private val repository: MemoTagRepository,
) {
	public suspend operator fun invoke(memoId: String?, tagId: String?): Result<Unit> {
		return runCatching {
			if (memoId.isNullOrBlank() || tagId.isNullOrBlank()) return@runCatching

			repository.upsert(memoId, tagId)
			pushMemoBackupQueueUseCase(memoId)
		}
	}
}
