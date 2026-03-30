package io.github.taetae98coding.diary.feature.memo.home

import io.github.taetae98coding.diary.domain.memo.usecase.HasListMemoFilterUseCase
import io.github.taetae98coding.diary.presenter.memo.api.ListMemoFilterStrategy
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class AccountMemoListFilterStrategy(private val hasListMemoFilterUseCase: HasListMemoFilterUseCase) : ListMemoFilterStrategy {

    override fun hasFilter(): Flow<Result<Boolean>> {
        return hasListMemoFilterUseCase()
    }
}
