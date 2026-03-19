package io.github.taetae98coding.diary.data.credentials.repository

import io.github.taetae98coding.diary.core.datastore.api.datasource.AccountMetaDataDataStoreDataSource
import io.github.taetae98coding.diary.core.datastore.api.entity.AccountMetaDataDataStoreEntity
import io.github.taetae98coding.diary.core.network.api.datasource.SessionRemoteDataSource
import io.github.taetae98coding.diary.core.network.api.entity.SessionRemoteEntity
import io.github.taetae98coding.diary.core.supabase.api.SupabaseAuth
import io.github.taetae98coding.diary.domain.credentials.repository.SessionRepository
import org.koin.core.annotation.Factory

@Factory
public class SessionRepositoryImpl(
    private val supabaseAuth: SupabaseAuth,
    private val sessionRemoteDataSource: SessionRemoteDataSource,
    private val accountMetaDataDataStoreDataSource: AccountMetaDataDataStoreDataSource,
) : SessionRepository {
    override suspend fun updateByGoogleAuthorizationCode(
        clientId: String,
        code: String,
        redirectUri: String,
    ) {
        val sessionRemote = sessionRemoteDataSource.getByGoogleAuthorizationCode(clientId, code, redirectUri)
        updateSession(sessionRemote)
    }

    override suspend fun updateByGoogleIdToken(idToken: String) {
        val sessionRemote = sessionRemoteDataSource.getByGoogleIdToken(idToken)
        updateSession(sessionRemote)
    }

    override suspend fun delete() {
        supabaseAuth.signOut()
    }

    private suspend fun updateSession(sessionRemote: SessionRemoteEntity) {
        accountMetaDataDataStoreDataSource.upsert(AccountMetaDataDataStoreEntity(profileImage = sessionRemote.account.profileImage))
        supabaseAuth.importAuthToken(sessionRemote.accessToken, sessionRemote.refreshToken)
    }
}
