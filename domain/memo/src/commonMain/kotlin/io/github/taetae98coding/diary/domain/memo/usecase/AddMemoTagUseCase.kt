package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.domain.memo.repository.AccountMemoTagRepository
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class AddMemoTagUseCase(
    private val requestSyncUseCase: RequestSyncUseCase,
    @param:Provided
    private val accountMemoTagRepository: AccountMemoTagRepository,
) {
    public suspend operator fun invoke(
        memoId: Uuid,
        tagId: Uuid,
    ): Result<Unit> {
        return runCatching {
            accountMemoTagRepository.upsertMemoTag(memoId, tagId, isMemoTag = true)
            requestSyncUseCase(SyncType.Background)
        }
    }
}
