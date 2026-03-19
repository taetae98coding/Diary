package io.github.taetae98coding.diary.core.supabase.impl

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.taetae98coding.diary.core.supabase.api.SupabaseAuth
import io.github.taetae98coding.diary.core.supabase.api.SupabaseSessionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@Factory
internal class SupabaseAuthImpl(private val supabase: SupabaseClient) : SupabaseAuth {
    override val sessionStatus: Flow<SupabaseSessionStatus>
        get() = supabase.auth.sessionStatus.mapLatest { status ->
            when (status) {
                is SessionStatus.Authenticated -> SupabaseSessionStatus.Authenticated(
                    userId = status.session.user?.id ?: "",
                    email = status.session.user?.email,
                )

                is SessionStatus.NotAuthenticated -> SupabaseSessionStatus.NotAuthenticated

                is SessionStatus.Initializing -> SupabaseSessionStatus.Loading

                is SessionStatus.RefreshFailure -> SupabaseSessionStatus.Loading
            }
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
    }
}
