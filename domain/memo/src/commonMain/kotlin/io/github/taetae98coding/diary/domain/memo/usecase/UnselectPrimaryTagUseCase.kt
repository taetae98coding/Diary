package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.domain.memo.repository.AccountMemoTagRepository
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory

@Factory
public class UnselectPrimaryTagUseCase(
    private val accountMemoTagRepository: AccountMemoTagRepository,
    private val requestSyncUseCase: RequestSyncUseCase,
) {
    public suspend operator fun invoke(memoId: Uuid): Result<Unit> {
        return runCatching {
            accountMemoTagRepository.updatePrimaryTag(memoId, null)
            requestSyncUseCase()
        }
    }
}
