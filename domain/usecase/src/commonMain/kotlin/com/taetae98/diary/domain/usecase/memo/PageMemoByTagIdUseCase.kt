package com.taetae98.diary.domain.usecase.memo

import app.cash.paging.PagingData
import com.taetae98.diary.domain.entity.account.Account
import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.core.FlowUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class PageMemoByTagIdUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val memoRepository: MemoRepository,
) : FlowUseCase<String?, PagingData<Memo>>() {
    override fun execute(params: String?): Flow<PagingData<Memo>> {
        if (params == null) return flowOf(PagingData.empty())

        return getAccountUseCase(Unit).mapLatest(Result<Account>::getOrThrow)
            .mapLatest { it.uid }
            .flatMapLatest { memoRepository.page(it, params) }
    }
}