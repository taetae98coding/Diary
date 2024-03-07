package com.taetae98.diary.domain.usecase.tag

import app.cash.paging.PagingData
import com.taetae98.diary.domain.entity.account.Account
import com.taetae98.diary.domain.entity.tag.Tag
import com.taetae98.diary.domain.exception.NoAccountException
import com.taetae98.diary.domain.repository.TagRepository
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
public class PageTagUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val tagRepository: TagRepository,
) : FlowUseCase<Unit, PagingData<Tag>>() {
    override fun execute(params: Unit): Flow<Result<PagingData<Tag>>> {
        return getAccountUseCase(Unit).map(Result<Account>::getOrNull)
            .flatMapLatest(::flatMapAccount)
    }

    private fun flatMapAccount(account: Account?): Flow<Result<PagingData<Tag>>> {
        if (account == null) return flowOf(Result.failure(NoAccountException()))

        return tagRepository.page(account.uid)
            .map { Result.success(it) }
    }
}