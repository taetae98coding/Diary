package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.domain.backup.usecase.PushMemoBackupQueueUseCase
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import org.koin.core.annotation.Factory

@Factory
public class UpdateMemoPrimaryTagUseCase internal constructor(
    private val pushMemoBackupQueueUseCase: PushMemoBackupQueueUseCase,
    private val repository: MemoRepository,
) {
    public suspend operator fun invoke(memoId: String?, tagId: String?): Result<Unit> {
        return runCatching {
            if (memoId.isNullOrBlank() || tagId.isNullOrBlank()) return@runCatching

            repository.updatePrimaryTag(memoId, tagId)
            pushMemoBackupQueueUseCase(memoId)
        }
    }
}
