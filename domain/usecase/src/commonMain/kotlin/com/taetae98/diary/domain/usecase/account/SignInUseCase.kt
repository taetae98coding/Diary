package com.taetae98.diary.domain.usecase.account

import com.taetae98.diary.domain.entity.account.account.Credential
import com.taetae98.diary.domain.repository.AccountRepository
import com.taetae98.diary.domain.usecase.core.UseCase
import org.koin.core.annotation.Factory

@Factory
public class SignInUseCase internal constructor(
    private val accountRepository: AccountRepository,
) : UseCase<Credential, Unit>() {
    override suspend fun execute(params: Credential) {
        accountRepository.signIn(params)
    }
}
