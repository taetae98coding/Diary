package com.taetae98.diary.domain.usecase.memo

import app.cash.paging.PagingData
import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.core.FlowUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import org.koin.core.annotation.Factory

@Factory
public class FindFinishedMemoUseCase internal constructor(
    private val memoRepository: MemoRepository,
    private val getAccountUseCase: GetAccountUseCase,
) : FlowUseCase<Unit, PagingData<Memo>>() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(params: Unit): Flow<Result<PagingData<Memo>>> {
        return getAccountUseCase(Unit)
            .mapNotNull { it.getOrNull() }
            .flatMapLatest { memoRepository.pageFinished(it.uid) }
            .map { Result.success(it) }
    }
}