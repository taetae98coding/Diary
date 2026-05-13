@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.taetae98coding.diary.data.account.repository

import io.github.taetae98coding.diary.core.model.account.AccountInfo
import io.github.taetae98coding.diary.core.supabase.api.SupabaseAuth
import io.github.taetae98coding.diary.core.supabase.api.SupabaseSessionStatus
import io.github.taetae98coding.diary.domain.account.repository.AccountInfoRepository
import kotlin.uuid.Uuid
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.runningFold
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
internal class AccountInfoRepositoryImpl(
    @param:Provided
    private val supabaseAuth: SupabaseAuth,
) : AccountInfoRepository {
    override fun get(): Flow<AccountInfo?> {
        return supabaseAuth.sessionStatus.runningFold<SupabaseSessionStatus, SupabaseSessionStatus>(SupabaseSessionStatus.Loading) { acc, value ->
            when (value) {
                is SupabaseSessionStatus.Authenticated, is SupabaseSessionStatus.NotAuthenticated -> value
                else -> acc
            }
        }.filter { status ->
            status is SupabaseSessionStatus.Authenticated || status is SupabaseSessionStatus.NotAuthenticated
        }.mapLatest { status ->
            when (status) {
                is SupabaseSessionStatus.Authenticated -> {
                    AccountInfo(
                        id = Uuid.parse(status.userId),
                        email = status.email ?: return@mapLatest null,
                        isAuthorized = true,
                    )
                }

                is SupabaseSessionStatus.NotAuthenticated -> {
                    AccountInfo(
                        id = Uuid.parse(status.userId ?: return@mapLatest null),
                        email = status.email ?: return@mapLatest null,
                        isAuthorized = false,
                    )
                }

                else -> null
            }
        }
    }
}
