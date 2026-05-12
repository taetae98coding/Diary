@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.memo.repository.AccountCalendarMemoFilterTagRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class HasCalendarMemoFilterUseCase(
    private val getAccountUseCase: GetAccountUseCase,
    @param:Provided
    private val accountCalendarMemoFilterTagRepository: AccountCalendarMemoFilterTagRepository,
) {
    public operator fun invoke(): Flow<Result<Boolean>> {
        return flow {
            getAccountUseCase().mapLatest { it.getOrThrow() }
                .flatMapLatest { accountCalendarMemoFilterTagRepository.get(it.accountId) }
                .mapLatest { it.isNotEmpty() }
                .also { emitAll(it) }
        }.map {
            Result.success(it)
        }.catch {
            emit(Result.failure(it))
        }
    }
}
