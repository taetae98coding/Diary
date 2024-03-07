package com.taetae98.diary.domain.usecase.memo

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
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.LocalDate
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class FindMemoByDateRangeUseCase(
    private val getAccountUseCase: GetAccountUseCase,
    private val memoRepository: MemoRepository,
) : FlowUseCase<ClosedRange<LocalDate>, List<Memo>>() {
    override fun execute(params: ClosedRange<LocalDate>): Flow<Result<List<Memo>>> {
        return getAccountUseCase(Unit)
            .mapLatest(Result<Account>::getOrNull)
            .flatMapLatest { flatMapAccount(it, params) }
    }

    private fun flatMapAccount(account: Account?, params: ClosedRange<LocalDate>): Flow<Result<List<Memo>>> {
        return if (account == null) {
            flowOf(Result.failure(NoAccountException()))
        } else {
            memoRepository.find(account.uid, params)
                .map { Result.success(it) }
        }
    }
}