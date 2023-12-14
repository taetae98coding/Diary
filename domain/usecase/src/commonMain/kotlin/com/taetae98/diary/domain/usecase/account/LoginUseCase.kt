package com.taetae98.diary.domain.usecase.account

import com.taetae98.diary.domain.repository.LoginRepository
import com.taetae98.diary.domain.usecase.core.UseCase
import org.koin.core.annotation.Factory

@Factory
public class LoginUseCase internal constructor(
    private val loginRepository: LoginRepository,
) : UseCase<String, Unit>() {
    override suspend fun execute(params: String) {
        loginRepository.signIn(params)
    }
}
