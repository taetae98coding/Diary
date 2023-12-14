package com.taetae98.diary.data.repository.login

import com.taetae98.diary.core.auth.api.AuthManager
import com.taetae98.diary.domain.repository.LoginRepository
import org.koin.core.annotation.Factory

@Factory
internal class LoginRepositoryImpl(
    private val authManager: AuthManager,
) : LoginRepository {
    override suspend fun signIn(idToken: String) {
        authManager.signIn(idToken)
    }
}