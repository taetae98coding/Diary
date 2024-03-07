package com.taetae98.diary.domain.usecase.memo

import app.cash.paging.PagingData
import com.taetae98.diary.domain.entity.account.Account
import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.exception.NoAccountException
import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.domain.repository.SelectTagByMemoRepository
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.core.FlowUseCase
import com.taetae98.diary.domain.usecase.tag.select.FindTagInMemoUseCase
import com.taetae98.diary.domain.usecase.tag.select.GetHasToPageNoTagMemoUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class PageMemoBySelectTagUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val findTagInMemoUseCase: FindTagInMemoUseCase,
    private val getHasToPageNoTagMemoUseCase: GetHasToPageNoTagMemoUseCase,
    private val memoRepository: MemoRepository,
    private val selectTagByMemoRepository: SelectTagByMemoRepository,
) : FlowUseCase<Unit, PagingData<Memo>>() {
    override fun execute(params: Unit): Flow<Result<PagingData<Memo>>> {
        return getAccountUseCase(Unit)
            .map(Result<Account>::getOrNull)
            .flatMapLatest(::page)
    }

    private fun page(account: Account?): Flow<Result<PagingData<Memo>>> {
        if (account == null) return flowOf(Result.failure(NoAccountException()))

        return combine(findTagInMemoUseCase(Unit), getHasToPageNoTagMemoUseCase(Unit)) { tagList, hasToPageNoTag ->
            tagList to hasToPageNoTag
        }.flatMapLatest { pair ->
            if (pair.first.isFailure || pair.second.isFailure) {
                flowOf(Result.failure(IllegalStateException()))
            } else {
                page(
                    account = account,
                    isTagEmpty = pair.first.getOrNull().orEmpty().isEmpty(),
                    hasToPageNoTag = pair.second.getOrNull() ?: false,
                ).map {
                    Result.success(it)
                }
            }
        }
    }

    private fun page(account: Account, isTagEmpty: Boolean, hasToPageNoTag: Boolean): Flow<PagingData<Memo>> {
        return if (isTagEmpty) {
            memoRepository.page(account.uid)
        } else {
            selectTagByMemoRepository.page(account.uid, hasToPageNoTag)
        }
    }
}
