@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.core.model.FilterPresence
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.memo.repository.AccountListMemoFilterTagRepository
import io.github.taetae98coding.diary.domain.memo.repository.ListMemoFilterOptionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class HasListMemoFilterUseCase(
    @param:Provided
    private val accountListMemoFilterTagRepository: AccountListMemoFilterTagRepository,
    @param:Provided
    private val listMemoFilterOptionRepository: ListMemoFilterOptionRepository,
    private val getAccountUseCase: GetAccountUseCase,
) {
    public operator fun invoke(): Flow<Result<Boolean>> {
        return flow {
            getAccountUseCase().mapLatest { it.getOrThrow() }
                .flatMapLatest { account ->
                    combine(
                        accountListMemoFilterTagRepository.get(account.accountId),
                        listMemoFilterOptionRepository.get(),
                    ) { tags, option ->
                        tags.isNotEmpty() || option.tagPresence != FilterPresence.NONE || option.datePresence != FilterPresence.NONE
                    }
                }
                .also { emitAll(it) }
        }.map {
            Result.success(it)
        }.catch {
            emit(Result.failure(it))
        }
    }
}
