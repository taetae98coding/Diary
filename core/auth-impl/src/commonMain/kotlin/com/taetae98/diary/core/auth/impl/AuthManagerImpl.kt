package com.taetae98.diary.core.auth.impl

import com.taetae98.diary.core.auth.api.AccountEntity
import com.taetae98.diary.core.auth.api.AuthManager
import com.taetae98.diary.core.auth.api.CredentialEntity
import com.taetae98.diary.library.firebase.auth.api.FirebaseAuthManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
internal class AuthManagerImpl(
    private val firebaseAuthManager: FirebaseAuthManager,
) : AuthManager {
    override suspend fun signIn(credential: CredentialEntity) {
        when (credential) {
            is CredentialEntity.Google -> firebaseAuthManager.signInWithGoogleToken(credential.idToken, credential.accessToken)
        }
    }

    override suspend fun signOut() {
        firebaseAuthManager.signOut()
    }

    override fun getAccount(): Flow<AccountEntity> {
        return firebaseAuthManager.getUser().map {
            if (it == null) {
                AccountEntity.Guest
            } else {
                AccountEntity.Member(
                    uid = it.uid,
                    email = it.email,
                )
            }
        }
    }
}