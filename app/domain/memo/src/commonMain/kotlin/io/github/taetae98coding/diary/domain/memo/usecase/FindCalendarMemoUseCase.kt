package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.LocalDate
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class FindCalendarMemoUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val repository: MemoRepository,
) {
    public operator fun invoke(dateRange: ClosedRange<LocalDate>): Flow<Result<List<Memo>>> {
        return flow {
            getAccountUseCase().mapLatest { it.getOrThrow() }
                .flatMapLatest { repository.findByDateRange(it.uid, dateRange) }
                .also { emitAll(it) }
        }.mapLatest {
            Result.success(it)
        }.catch {
            emit(Result.failure(it))
        }
    }
}
