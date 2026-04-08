package io.github.taetae98coding.diary.feature.tag.memo

import androidx.paging.PagingData
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.navigation.argument.TagId
import io.github.taetae98coding.diary.domain.memo.usecase.DeleteMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.FinishMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.PageMemoByTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.RestartMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.RestoreMemoUseCase
import io.github.taetae98coding.diary.presenter.memo.api.MemoListStrategy
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam

@Factory
internal class AccountTagMemoListStrategy(
    @param:InjectedParam
    private val tagId: TagId,
    private val pageMemoByTagUseCase: PageMemoByTagUseCase,
    private val finishMemoUseCase: FinishMemoUseCase,
    private val restartMemoUseCase: RestartMemoUseCase,
    private val deleteMemoUseCase: DeleteMemoUseCase,
    private val restoreMemoUseCase: RestoreMemoUseCase,
) : MemoListStrategy {
    override fun page(): Flow<Result<PagingData<Memo>>> {
        return pageMemoByTagUseCase(tagId.value)
    }

    override suspend fun finish(memoId: Uuid): Result<Unit> {
        return finishMemoUseCase(memoId)
    }

    override suspend fun restart(memoId: Uuid): Result<Unit> {
        return restartMemoUseCase(memoId)
    }

    override suspend fun delete(memoId: Uuid): Result<Unit> {
        return deleteMemoUseCase(memoId)
    }

    override suspend fun restore(memoId: Uuid): Result<Unit> {
        return restoreMemoUseCase(memoId)
    }
}
