package io.github.taetae98coding.diary.domain.calendar.usecase

import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import io.github.taetae98coding.diary.domain.tag.repository.TagRepository
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
    private val memoRepository: MemoRepository,
    private val tagRepository: TagRepository,
) {
    public operator fun invoke(dateRange: ClosedRange<LocalDate>): Flow<Result<List<Memo>>> {
        return flow {
            getAccountUseCase().mapLatest { it.getOrThrow() }
                .flatMapLatest { memoRepository.findByDateRange(it.uid, dateRange) }
                .flatMapLatest { memoList ->
                    val tagIdSet = memoList.mapNotNull { it.primaryTag }.toSet()

                    tagRepository.findByIds(tagIdSet).mapLatest { tagList ->
                        memoList.map { memo ->
                            val color = tagList.find { it.id == memo.primaryTag }
                                ?.detail
                                ?.color
                                ?: memo.detail.color

                            memo.copy(detail = memo.detail.copy(color = color))
                        }
                    }
                }
                .also { emitAll(it) }
        }.mapLatest {
            Result.success(it)
        }.catch {
            it.printStackTrace()
            emit(Result.failure(it))
        }
    }
}
