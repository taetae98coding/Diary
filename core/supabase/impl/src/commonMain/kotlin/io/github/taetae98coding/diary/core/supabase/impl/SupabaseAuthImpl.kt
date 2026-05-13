@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.taetae98coding.diary.core.supabase.impl

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.event.AuthEvent
import io.github.jan.supabase.auth.status.RefreshFailureCause
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.taetae98coding.diary.core.supabase.api.SupabaseAuth
import io.github.taetae98coding.diary.core.supabase.api.SupabaseSessionStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@OptIn(SupabaseExperimental::class)
@Factory
internal class SupabaseAuthImpl(private val supabase: SupabaseClient) : SupabaseAuth {
    override val sessionStatus: Flow<SupabaseSessionStatus>
        get() = supabase.auth.sessionStatus.mapLatest { status ->
            when (status) {
                is SessionStatus.Authenticated -> {
                    SupabaseSessionStatus.Authenticated(
                        userId = status.session.user?.id.orEmpty(),
                        email = status.session.user?.email,
                    )
                }

                is SessionStatus.NotAuthenticated -> getSessionStatusFromStorage() ?: SupabaseSessionStatus.NotAuthenticated.NotLogin

                is SessionStatus.Initializing -> getSessionStatusFromStorage() ?: SupabaseSessionStatus.Loading

                is SessionStatus.RefreshFailure -> {
                    val cause = (supabase.auth.events.first() as? AuthEvent.RefreshFailure)?.cause
                    resolveRefreshFailure(cause)
                }
            }
        }

    private suspend fun resolveRefreshFailure(cause: RefreshFailureCause?): SupabaseSessionStatus.NotAuthenticated {
        return when (cause) {
            is RefreshFailureCause.InternalServerError -> SupabaseSessionStatus.NotAuthenticated.NotLogin
            else -> getSessionStatusFromStorage() ?: SupabaseSessionStatus.NotAuthenticated.NotLogin
        }
    }

    private suspend fun getSessionStatusFromStorage(): SupabaseSessionStatus.NotAuthenticated? {
        return runCatching {
            supabase.auth.sessionManager.loadSession().user
                ?.let { SupabaseSessionStatus.NotAuthenticated(userId = it.id, email = it.email) }
        }.getOrNull()
    }

    override suspend fun signOut() {
        supabase.auth.signOut()
    }

    override suspend fun importAuthToken(
        accessToken: String,
        refreshToken: String,
        retrieveUser: Boolean,
        autoRefresh: Boolean,
    ) {
        supabase.auth.importAuthToken(
            accessToken = accessToken,
            refreshToken = refreshToken,
            retrieveUser = retrieveUser,
            autoRefresh = autoRefresh,
        )
        supabase.auth.sessionStatus.first { it is SessionStatus.Authenticated }
    }
}
