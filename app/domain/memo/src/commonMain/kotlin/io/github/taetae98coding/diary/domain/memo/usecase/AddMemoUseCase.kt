package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.common.exception.memo.MemoTitleBlankException
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.backup.usecase.PushMemoBackupQueueUseCase
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import org.koin.core.annotation.Factory

@OptIn(ExperimentalUuidApi::class)
@Factory
public class AddMemoUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val pushMemoBackupQueueUseCase: PushMemoBackupQueueUseCase,
    private val clock: Clock,
    private val repository: MemoRepository,
) {
    public suspend operator fun invoke(
        detail: MemoDetail,
        primaryTag: String?,
        tagIds: Set<String>,
    ): Result<Unit> {
        return runCatching {
            if (detail.title.isBlank()) throw MemoTitleBlankException()

            val memoId = Uuid.random().toString()
            val account = getAccountUseCase().first().getOrThrow()
            val memo = Memo(
                id = memoId,
                detail = detail,
                primaryTag = primaryTag,
                owner = account.uid,
                isFinish = false,
                isDelete = false,
                updateAt = clock.now(),
            )

            repository.upsert(memo, tagIds)
            pushMemoBackupQueueUseCase(memoId)
        }
    }
}
