package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.core.model.memo.CalendarMemo
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.memo.repository.AccountCalendarMemoRepository
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
public class GetCalendarMemoUseCase(
    private val getAccountUseCase: GetAccountUseCase,
    @param:Provided
    private val accountCalendarMemoRepository: AccountCalendarMemoRepository,
) {
    public operator fun invoke(year: Int): Flow<Result<List<CalendarMemo>>> {
        return flow {
            getAccountUseCase().mapLatest(Result<Account>::getOrThrow)
                .flatMapLatest { accountCalendarMemoRepository.get(accountId = it.accountId, year = year) }
                .also { emitAll(it) }
        }.map {
            Result.success(it)
        }.catch {
            emit(Result.failure(it))
        }
    }
}
