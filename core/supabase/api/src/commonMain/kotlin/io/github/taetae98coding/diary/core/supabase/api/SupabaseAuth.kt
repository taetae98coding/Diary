package io.github.taetae98coding.diary.core.supabase.api

import kotlinx.coroutines.flow.Flow

public interface SupabaseAuth {
    public val sessionStatus: Flow<SupabaseSessionStatus>

    public suspend fun signOut()

    public suspend fun importAuthToken(
        accessToken: String,
        refreshToken: String,
        retrieveUser: Boolean = true,
        autoRefresh: Boolean = true,
    )
}
