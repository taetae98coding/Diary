package com.taetae98.diary.domain.usecase.memo

import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.domain.usecase.core.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import org.koin.core.annotation.Factory

@Factory
public class FindByDateRangeUseCase(
    private val memoRepository: MemoRepository,
) : FlowUseCase<FindByDateRangeUseCase.Params, List<Memo>>() {
    override fun execute(params: Params): Flow<List<Memo>> {
        return memoRepository.find(params.ownerId, params.dateRange)
    }

    public data class Params(
        val ownerId: String?,
        val dateRange: ClosedRange<LocalDate>,
    )
}