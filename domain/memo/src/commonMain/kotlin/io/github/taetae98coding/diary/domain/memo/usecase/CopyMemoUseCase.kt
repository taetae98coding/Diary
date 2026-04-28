package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import io.github.taetae98coding.diary.domain.memo.repository.MemoTagRepository
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class CopyMemoUseCase(
    private val addMemoUseCase: AddMemoUseCase,
    @param:Provided
    private val memoRepository: MemoRepository,
    @param:Provided
    private val memoTagRepository: MemoTagRepository,
) {
    public suspend operator fun invoke(memoId: Uuid): Result<Unit> {
        return runCatching {
            val source = requireNotNull(memoRepository.get(memoId).first())
            val memoTagIds = memoTagRepository.getMemoTag(memoId).first()
                .map { it.id }
                .toSet()

            addMemoUseCase(
                detail = source.detail,
                primaryTag = source.primaryTag,
                memoTagIds = memoTagIds,
            ).getOrThrow()
        }
    }
}
