package io.github.taetae98coding.diary.feature.memo.add

import androidx.paging.PagingData
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.domain.memo.usecase.AddMemoUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.PageTagUseCase
import io.github.taetae98coding.diary.presenter.memo.api.MemoAddStrategy
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class AccountMemoAddStrategy(
    private val addMemoUseCase: AddMemoUseCase,
    private val pageTagUseCase: PageTagUseCase,
) : MemoAddStrategy {
    override suspend fun add(
        detail: MemoDetail,
        primaryTag: Uuid?,
        memoTagIds: Set<Uuid>,
    ): Result<Unit> {
        return addMemoUseCase(
            detail = detail,
            primaryTag = primaryTag,
            memoTagIds = memoTagIds,
        )
    }

    override fun pageTag(): Flow<Result<PagingData<Tag>>> {
        return pageTagUseCase()
    }
}
