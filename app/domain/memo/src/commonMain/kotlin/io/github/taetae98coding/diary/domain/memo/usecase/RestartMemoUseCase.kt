package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.domain.backup.usecase.PushMemoBackupQueueUseCase
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import org.koin.core.annotation.Factory

@Factory
public class RestartMemoUseCase internal constructor(private val pushMemoBackupQueueUseCase: PushMemoBackupQueueUseCase, private val repository: MemoRepository) {
	public suspend operator fun invoke(memoId: String?): Result<Unit> {
		return runCatching {
			if (memoId.isNullOrBlank()) return@runCatching

			repository.updateFinish(memoId, false)
			pushMemoBackupQueueUseCase(memoId)
		}
	}
}
