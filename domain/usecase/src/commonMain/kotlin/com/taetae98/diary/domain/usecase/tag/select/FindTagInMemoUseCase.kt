package com.taetae98.diary.domain.usecase.tag.select

import com.taetae98.diary.domain.entity.account.Account
import com.taetae98.diary.domain.entity.tag.Tag
import com.taetae98.diary.domain.exception.NoAccountException
import com.taetae98.diary.domain.repository.SelectTagByMemoRepository
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
public class FindTagInMemoUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val selectTagByMemoRepository: SelectTagByMemoRepository,
) : FlowUseCase<Unit, List<Tag>>() {
    override fun execute(params: Unit): Flow<Result<List<Tag>>> {
        return getAccountUseCase(Unit).map { it.getOrNull() }
            .flatMapLatest(::flatMapAccount)
    }

    private fun flatMapAccount(account: Account?): Flow<Result<List<Tag>>> {
        if (account == null) return flowOf(Result.failure(NoAccountException()))

        return selectTagByMemoRepository.find(account.uid)
            .map { Result.success(it) }
    }
}
