package com.taetae98.diary.domain.usecase.tag.select

import com.taetae98.diary.domain.entity.account.Account
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
public class GetHasToPageNoTagMemoUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val selectTagByMemoRepository: SelectTagByMemoRepository,
) : FlowUseCase<Unit, Boolean>() {
    override fun execute(params: Unit): Flow<Result<Boolean>> {
        return getAccountUseCase(Unit).map(Result<Account>::getOrNull)
            .flatMapLatest(::flatMapAccount)
    }

    private fun flatMapAccount(account: Account?): Flow<Result<Boolean>> {
        if (account == null) return flowOf(Result.failure(NoAccountException()))

        return selectTagByMemoRepository.hasToPageNoTagMemo(account.uid)
            .map { Result.success(it) }
    }
}
