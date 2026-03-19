package io.github.taetae98coding.diary.feature.memo.detail

import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.domain.memo.usecase.DeleteMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.FinishMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.GetMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.RestartMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.UpdateMemoUseCase
import io.github.taetae98coding.diary.presenter.memo.api.MemoDetailStrategy
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class AccountMemoDetailStrategy(
    private val getMemoUseCase: GetMemoUseCase,
    private val updateMemoUseCase: UpdateMemoUseCase,
    private val finishMemoUseCase: FinishMemoUseCase,
    private val restartMemoUseCase: RestartMemoUseCase,
    private val deleteMemoUseCase: DeleteMemoUseCase,
) : MemoDetailStrategy {
    override fun get(memoId: Uuid): Flow<Result<Memo?>> {
        return getMemoUseCase(memoId)
    }

    override suspend fun update(
        memoId: Uuid,
        detail: MemoDetail,
    ): Result<Unit> {
        return updateMemoUseCase(memoId, detail)
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
}
