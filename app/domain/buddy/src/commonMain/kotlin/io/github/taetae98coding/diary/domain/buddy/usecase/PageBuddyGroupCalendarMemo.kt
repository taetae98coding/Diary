package io.github.taetae98coding.diary.domain.buddy.usecase

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.buddy.repository.BuddyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.LocalDate
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class PageBuddyGroupCalendarMemo internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val repository: BuddyRepository,
) {
    public operator fun invoke(groupId: String, dateRange: ClosedRange<LocalDate>): Flow<Result<List<Memo>>> {
        return getAccountUseCase().mapLatest { it.getOrThrow() }
            .flatMapLatest { account ->
                if (account is Account.Guest) {
                    flowOf(emptyList())
                } else {
                    repository.findMemoByDateRange(groupId, dateRange)
                }
            }
            .mapLatest { Result.success(it) }
            .catch { emit(Result.failure(it)) }
    }
}
