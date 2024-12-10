package io.github.taetae98coding.diary.domain.buddy.usecase

import io.github.taetae98coding.diary.core.model.Memo
import io.github.taetae98coding.diary.domain.buddy.repository.BuddyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.LocalDate
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class FindBuddyGroupMemoByDate internal constructor(
    private val repository: BuddyRepository,
) {
    public operator fun invoke(groupId: String, dateRange: ClosedRange<LocalDate>): Flow<Result<List<Memo>>> {
        return flow { emitAll(repository.findMemoByDate(groupId, dateRange)) }
            .mapLatest { Result.success(it) }
            .catch { emit(Result.failure(it)) }
    }
}
