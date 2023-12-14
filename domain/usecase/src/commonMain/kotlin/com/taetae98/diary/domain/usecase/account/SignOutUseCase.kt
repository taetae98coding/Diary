package com.taetae98.diary.domain.usecase.account

import com.taetae98.diary.domain.repository.AccountRepository
import com.taetae98.diary.domain.usecase.core.UseCase
import org.koin.core.annotation.Factory

@Factory
public class SignOutUseCase internal constructor(
    private val accountRepository: AccountRepository,
) : UseCase<Unit, Unit>() {
    override suspend fun execute(params: Unit) {
        accountRepository.signOut()
    }
}