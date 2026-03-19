package io.github.taetae98coding.diary.feature.memo.add

import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.domain.memo.usecase.AddMemoUseCase
import io.github.taetae98coding.diary.presenter.memo.api.MemoAddStrategy
import org.koin.core.annotation.Factory

@Factory
internal class AccountMemoAddStrategy(private val addMemoUseCase: AddMemoUseCase) : MemoAddStrategy {
    override suspend fun add(detail: MemoDetail): Result<Unit> {
        return addMemoUseCase(detail)
    }
}
