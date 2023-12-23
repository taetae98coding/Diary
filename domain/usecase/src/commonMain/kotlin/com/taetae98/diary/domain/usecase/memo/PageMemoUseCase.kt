package com.taetae98.diary.domain.usecase.memo

import app.cash.paging.PagingData
import com.taetae98.diary.domain.entity.account.memo.Memo
import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.domain.usecase.core.FlowUseCase
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
public class PageMemoUseCase internal constructor(
    private val memoRepository: MemoRepository,
) : FlowUseCase<Unit, PagingData<Memo>>() {
    override fun execute(params: Unit): Flow<PagingData<Memo>> {
        return memoRepository.page()
    }
}