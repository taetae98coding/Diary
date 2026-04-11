package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.domain.memo.repository.AccountMemoRepository
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class FinishMemoUseCase(
    private val requestSyncUseCase: RequestSyncUseCase,
    @param:Provided
    private val accountMemoRepository: AccountMemoRepository,
) {
    public suspend operator fun invoke(memoId: Uuid): Result<Unit> {
        return runCatching {
            accountMemoRepository.updateFinish(memoId = memoId, isFinished = true)

            requestSyncUseCase(SyncType.Background)
        }
    }
}
