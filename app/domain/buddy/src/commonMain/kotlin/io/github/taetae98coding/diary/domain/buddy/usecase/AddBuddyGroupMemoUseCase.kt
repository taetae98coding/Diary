package io.github.taetae98coding.diary.domain.buddy.usecase

import io.github.taetae98coding.diary.common.exception.memo.MemoTitleBlankException
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.domain.buddy.repository.BuddyRepository
import kotlinx.datetime.Clock
import org.koin.core.annotation.Factory

@Factory
public class AddBuddyGroupMemoUseCase internal constructor(
    private val clock: Clock,
    private val repository: BuddyRepository,
) {
    public suspend operator fun invoke(
        groupId: String?,
        detail: MemoDetail,
        primaryTag: String?,
        tagIds: Set<String>,
    ): Result<Unit> {
        return runCatching {
            if (groupId.isNullOrBlank()) return@runCatching
            if (detail.title.isBlank()) throw MemoTitleBlankException()

            val memo = Memo(
                id = repository.getNextMemoId(),
                detail = detail,
                primaryTag = primaryTag,
                owner = groupId,
                isFinish = false,
                isDelete = false,
                updateAt = clock.now(),
            )

            repository.upsert(groupId, memo, tagIds)
        }
    }
}
