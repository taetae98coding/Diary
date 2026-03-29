package io.github.taetae98coding.diary.feature.memo.detail

import androidx.paging.PagingData
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.domain.memo.usecase.AddMemoTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.GetMemoTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.GetMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.RemoveMemoTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.SelectPrimaryTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.UnselectPrimaryTagUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.PageTagUseCase
import io.github.taetae98coding.diary.presenter.memo.api.MemoDetailTagStrategy
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class AccountMemoDetailTagStrategy(
    private val getMemoUseCase: GetMemoUseCase,
    private val pageTagUseCase: PageTagUseCase,
    private val getMemoTagUseCase: GetMemoTagUseCase,
    private val selectPrimaryTagUseCase: SelectPrimaryTagUseCase,
    private val unselectPrimaryTagUseCase: UnselectPrimaryTagUseCase,
    private val addMemoTagUseCase: AddMemoTagUseCase,
    private val removeMemoTagUseCase: RemoveMemoTagUseCase,
) : MemoDetailTagStrategy {
    override fun get(memoId: Uuid): Flow<Result<Memo?>> {
        return getMemoUseCase(memoId)
    }

    override fun pageTag(): Flow<Result<PagingData<Tag>>> {
        return pageTagUseCase()
    }

    override fun getMemoTag(memoId: Uuid): Flow<Result<List<Tag>>> {
        return getMemoTagUseCase(memoId)
    }

    override suspend fun selectPrimaryTag(
        memoId: Uuid,
        tagId: Uuid,
    ): Result<Unit> {
        return selectPrimaryTagUseCase(memoId, tagId)
    }

    override suspend fun unselectPrimaryTag(memoId: Uuid): Result<Unit> {
        return unselectPrimaryTagUseCase(memoId)
    }

    override suspend fun addMemoTag(
        memoId: Uuid,
        tagId: Uuid,
    ): Result<Unit> {
        return addMemoTagUseCase(memoId, tagId)
    }

    override suspend fun removeMemoTag(
        memoId: Uuid,
        tagId: Uuid,
    ): Result<Unit> {
        return removeMemoTagUseCase(memoId, tagId)
    }
}
