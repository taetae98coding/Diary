package com.taetae98.diary.domain.usecase.memo

import app.cash.paging.PagingData
import com.taetae98.diary.domain.entity.account.Account
import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.entity.tag.TagId
import com.taetae98.diary.domain.exception.NoAccountException
import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.core.FlowUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class PageMemoByTagIdUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val memoRepository: MemoRepository,
) : FlowUseCase<TagId, PagingData<Memo>>() {
    override fun execute(params: TagId): Flow<Result<PagingData<Memo>>> {
        if (params.isInvalid()) return flowOf(Result.success(PagingData.empty()))

        return getAccountUseCase(Unit).mapLatest(Result<Account>::getOrNull)
            .flatMapLatest { flatMapAccount(it, params) }
    }

    private fun flatMapAccount(account: Account?, tagId: TagId): Flow<Result<PagingData<Memo>>> {
        if (account == null) return flowOf(Result.failure(NoAccountException()))

        return memoRepository.page(account.uid, tagId.value)
            .map { Result.success(it) }
    }
}