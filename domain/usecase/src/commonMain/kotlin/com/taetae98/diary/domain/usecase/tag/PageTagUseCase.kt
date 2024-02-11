package com.taetae98.diary.domain.usecase.tag

import app.cash.paging.PagingData
import com.taetae98.diary.domain.entity.account.Account
import com.taetae98.diary.domain.entity.tag.Tag
import com.taetae98.diary.domain.repository.TagRepository
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.core.FlowUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@Factory
public class PageTagUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val tagRepository: TagRepository,
) : FlowUseCase<Unit, PagingData<Tag>>() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(params: Unit): Flow<PagingData<Tag>> {
        return getAccountUseCase(Unit).mapLatest(Result<Account>::getOrThrow)
            .mapLatest { it.uid }
            .flatMapLatest(tagRepository::page)
    }
}