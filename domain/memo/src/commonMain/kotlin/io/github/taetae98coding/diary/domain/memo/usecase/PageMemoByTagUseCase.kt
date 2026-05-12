@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.taetae98coding.diary.domain.memo.usecase

import androidx.paging.PagingData
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.memo.repository.AccountMemoRepository
import kotlin.uuid.Uuid
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
public class PageMemoByTagUseCase(
    private val getAccountUseCase: GetAccountUseCase,
    @param:Provided
    private val accountMemoRepository: AccountMemoRepository,
) {
    public operator fun invoke(tagId: Uuid): Flow<Result<PagingData<Memo>>> {
        return flow {
            getAccountUseCase().mapLatest { it.getOrThrow() }
                .flatMapLatest { accountMemoRepository.pageByTag(it.accountId, tagId) }
                .also { emitAll(it) }
        }.map {
            Result.success(it)
        }.catch {
            emit(Result.failure(it))
        }
    }
}
