package com.taetae98.diary.domain.usecase.memo

import app.cash.paging.PagingData
import com.taetae98.diary.domain.entity.account.Account
import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.exception.NoAccountException
import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.core.FlowUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class PageFinishMemoUseCase internal constructor(
    private val memoRepository: MemoRepository,
    private val getAccountUseCase: GetAccountUseCase,
) : FlowUseCase<Unit, PagingData<Memo>>() {
    override fun execute(params: Unit): Flow<Result<PagingData<Memo>>> {
        return getAccountUseCase(Unit)
            .map(Result<Account>::getOrNull)
            .flatMapLatest(::flatMapAccount)
    }

    private fun flatMapAccount(account: Account?): Flow<Result<PagingData<Memo>>> {
        return if (account == null) {
            flowOf(Result.failure(NoAccountException()))
        } else {
            memoRepository.pageFinished(account.uid)
                .map { Result.success(it) }
        }
    }
}