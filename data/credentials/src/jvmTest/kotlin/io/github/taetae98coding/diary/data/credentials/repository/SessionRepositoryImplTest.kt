package io.github.taetae98coding.diary.data.credentials.repository

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.github.taetae98coding.diary.core.datastore.api.datasource.AccountMetaDataDataStoreDataSource
import io.github.taetae98coding.diary.core.datastore.api.entity.AccountMetaDataDataStoreEntity
import io.github.taetae98coding.diary.core.network.api.datasource.SessionRemoteDataSource
import io.github.taetae98coding.diary.core.network.api.entity.SessionRemoteEntity
import io.github.taetae98coding.diary.core.supabase.api.SupabaseAuth
import io.kotest.core.spec.style.FunSpec
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.mockk

class SessionRepositoryImplTest : FunSpec() {
    private val supabaseAuth = mockk<SupabaseAuth>(relaxUnitFun = true)
    private val sessionRemoteDataSource = mockk<SessionRemoteDataSource>()
    private val accountMetaDataDataStoreDataSource = mockk<AccountMetaDataDataStoreDataSource>(relaxUnitFun = true)
    private val repository = SessionRepositoryImpl(
        supabaseAuth,
        sessionRemoteDataSource,
        accountMetaDataDataStoreDataSource,
    )

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        beforeTest { clearAllMocks() }

        test("updateByGoogleIdToken - session을 가져온 후 metadata를 저장하고 auth token을 import한다") {
            val idToken = "test-id-token"
            val sessionRemote = fixtureMonkey.giveMeOne<SessionRemoteEntity>()

            coEvery { sessionRemoteDataSource.getByGoogleIdToken(idToken) } returns sessionRemote

            repository.updateByGoogleIdToken(idToken)

            coVerifyOrder {
                sessionRemoteDataSource.getByGoogleIdToken(idToken)
                accountMetaDataDataStoreDataSource.upsert(
                    AccountMetaDataDataStoreEntity(profileImage = sessionRemote.account.profileImage),
                )
                supabaseAuth.importAuthToken(sessionRemote.accessToken, sessionRemote.refreshToken)
            }
        }

        test("updateByGoogleAuthorizationCode - session을 가져온 후 metadata를 저장하고 auth token을 import한다") {
            val clientId = "test-client-id"
            val code = "test-code"
            val redirectUri = "test-redirect-uri"
            val sessionRemote = fixtureMonkey.giveMeOne<SessionRemoteEntity>()

            coEvery { sessionRemoteDataSource.getByGoogleAuthorizationCode(clientId, code, redirectUri) } returns sessionRemote

            repository.updateByGoogleAuthorizationCode(clientId, code, redirectUri)

            coVerifyOrder {
                sessionRemoteDataSource.getByGoogleAuthorizationCode(clientId, code, redirectUri)
                accountMetaDataDataStoreDataSource.upsert(
                    AccountMetaDataDataStoreEntity(profileImage = sessionRemote.account.profileImage),
                )
                supabaseAuth.importAuthToken(sessionRemote.accessToken, sessionRemote.refreshToken)
            }
        }

        test("delete - signOut을 호출한다") {
            repository.delete()

            coVerify(exactly = 1) { supabaseAuth.signOut() }
        }
    }
}
