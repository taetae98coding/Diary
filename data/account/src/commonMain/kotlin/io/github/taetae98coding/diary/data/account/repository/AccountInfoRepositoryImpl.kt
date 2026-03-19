package io.github.taetae98coding.diary.data.account.repository

import io.github.taetae98coding.diary.core.model.account.AccountInfo
import io.github.taetae98coding.diary.core.supabase.api.SupabaseAuth
import io.github.taetae98coding.diary.core.supabase.api.SupabaseSessionStatus
import io.github.taetae98coding.diary.domain.account.repository.AccountInfoRepository
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.runningFold
import org.koin.core.annotation.Factory

@Factory
internal class AccountInfoRepositoryImpl(private val supabaseAuth: SupabaseAuth) : AccountInfoRepository {
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
                is SupabaseSessionStatus.Authenticated -> AccountInfo(
                    id = Uuid.parse(status.userId),
                    email = requireNotNull(status.email),
                )

                is SupabaseSessionStatus.NotAuthenticated -> null

                else -> null
            }
        }
    }
}
