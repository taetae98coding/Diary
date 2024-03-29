package com.taetae98.diary.domain.usecase.account

import com.taetae98.diary.domain.entity.account.Account
import com.taetae98.diary.domain.repository.AccountRepository
import com.taetae98.diary.domain.usecase.core.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
public class GetAccountUseCase internal constructor(
    private val accountRepository: AccountRepository,
) : FlowUseCase<Unit, Account>() {
    override fun execute(params: Unit): Flow<Result<Account>> {
        return accountRepository
            .getAccount()
            .map { Result.success(it) }
    }
}