package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.domain.backup.usecase.PushMemoBackupQueueUseCase
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import org.koin.core.annotation.Factory

@Factory
public class FinishMemoUseCase internal constructor(private val repository: MemoRepository, private val pushMemoBackupQueueUseCase: PushMemoBackupQueueUseCase) {
	public suspend operator fun invoke(memoId: String?): Result<Unit> {
		return runCatching {
			if (memoId.isNullOrBlank()) return@runCatching

			repository.updateFinish(memoId, true)
			pushMemoBackupQueueUseCase(memoId)
		}
	}
}
