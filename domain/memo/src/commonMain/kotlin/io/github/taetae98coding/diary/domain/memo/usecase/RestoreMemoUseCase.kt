package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.memo.repository.AccountMemoRepository
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class RestoreMemoUseCase(
    private val getAccountUseCase: GetAccountUseCase,
    private val requestSyncUseCase: RequestSyncUseCase,
    @param:Provided
    private val accountMemoRepository: AccountMemoRepository,
) {
    public suspend operator fun invoke(memoId: Uuid): Result<Unit> {
        return runCatching {
            val account = getAccountUseCase().first().getOrThrow()

            accountMemoRepository.updateDelete(memoId = memoId, isDeleted = false)

            requestSyncUseCase(SyncType.Background)
        }
    }
}
