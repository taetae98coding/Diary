package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.model.memo.MemoTitleBlankException
import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.memo.repository.AccountMemoRepository
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class AddMemoUseCase(
    private val getAccountUseCase: GetAccountUseCase,
    private val accountMemoRepository: AccountMemoRepository,
    private val requestSyncUseCase: RequestSyncUseCase,
) {
    public suspend operator fun invoke(
        detail: MemoDetail,
        primaryTag: Uuid? = null,
        memoTagIds: Set<Uuid> = emptySet(),
    ): Result<Unit> {
        return runCatching {
            if (detail.title.isBlank()) throw MemoTitleBlankException()

            val account = getAccountUseCase().first().getOrThrow()

            accountMemoRepository.add(
                accountId = account.accountId,
                detail = detail,
                primaryTag = primaryTag,
                memoTagIds = memoTagIds,
            )

            requestSyncUseCase(SyncType.Background)
        }
    }
}
