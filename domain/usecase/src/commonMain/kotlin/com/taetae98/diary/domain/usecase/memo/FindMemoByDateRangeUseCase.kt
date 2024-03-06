package com.taetae98.diary.domain.usecase.memo

import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.domain.usecase.core.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import org.koin.core.annotation.Factory

@Factory
public class FindMemoByDateRangeUseCase(
    private val memoRepository: MemoRepository,
) : FlowUseCase<FindMemoByDateRangeUseCase.Params, List<Memo>>() {
    override fun execute(params: Params): Flow<Result<List<Memo>>> {
        return memoRepository
            .find(params.ownerId, params.dateRange)
            .map { Result.success(it) }
    }

    public data class Params(
        val ownerId: String?,
        val dateRange: ClosedRange<LocalDate>,
    )
}