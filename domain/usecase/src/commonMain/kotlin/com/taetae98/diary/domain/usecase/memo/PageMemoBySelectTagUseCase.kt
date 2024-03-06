package com.taetae98.diary.domain.usecase.memo

import app.cash.paging.PagingData
import com.taetae98.diary.domain.entity.account.Account
import com.taetae98.diary.domain.entity.memo.Memo
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
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class PageMemoBySelectTagUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val memoRepository: MemoRepository,
    private val findTagInMemoUseCase: FindTagInMemoUseCase,
    private val selectTagByMemoRepository: SelectTagByMemoRepository,
    private val getHasToPageNoTagMemoUseCase: GetHasToPageNoTagMemoUseCase,
) : FlowUseCase<Unit, PagingData<Memo>>() {
    override fun execute(params: Unit): Flow<PagingData<Memo>> {
        return getAccountUseCase(Unit).mapLatest(Result<Account>::getOrThrow)
            .mapLatest { it.uid }
            .flatMapLatest(::page)
    }

    private fun page(uid: String?): Flow<PagingData<Memo>> {
        return combine(
            findTagInMemoUseCase(Unit).mapLatest { it.getOrNull().orEmpty() },
            getHasToPageNoTagMemoUseCase(Unit).mapLatest { it.getOrNull() ?: false },
        ) { tagList, hasToPage ->
            if (tagList.isEmpty()) {
                memoRepository.page(uid)
            } else {
                selectTagByMemoRepository.page(uid, hasToPage)
            }
        }.flatMapLatest {
            it
        }
    }
}
