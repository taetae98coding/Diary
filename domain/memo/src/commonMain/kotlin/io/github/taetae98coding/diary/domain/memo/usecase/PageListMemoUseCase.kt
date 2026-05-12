@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.taetae98coding.diary.domain.memo.usecase

import androidx.paging.PagingData
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.memo.repository.AccountListMemoRepository
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
public class PageListMemoUseCase(
    private val getAccountUseCase: GetAccountUseCase,
    @param:Provided
    private val accountListMemoRepository: AccountListMemoRepository,
    @param:Provided
    private val listMemoFilterOptionRepository: ListMemoFilterOptionRepository,
) {
    public operator fun invoke(): Flow<Result<PagingData<Memo>>> {
        return flow {
            combine(
                getAccountUseCase().mapLatest { it.getOrThrow() },
                listMemoFilterOptionRepository.get(),
            ) { account, option ->
                Pair(account, option)
            }.flatMapLatest { (account, option) ->
                accountListMemoRepository.page(
                    accountId = account.accountId,
                    tagPresence = option.tagPresence,
                    datePresence = option.datePresence,
                )
            }.also { emitAll(it) }
        }.map {
            Result.success(it)
        }.catch {
            emit(Result.failure(it))
        }
    }
}
