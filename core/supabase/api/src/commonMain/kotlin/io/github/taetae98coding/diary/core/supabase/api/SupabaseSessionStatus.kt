package io.github.taetae98coding.diary.core.supabase.api

public sealed interface SupabaseSessionStatus {
    public data class Authenticated(
        val userId: String,
        val email: String?,
    ) : SupabaseSessionStatus

    public data object NotAuthenticated : SupabaseSessionStatus
    public data object Loading : SupabaseSessionStatus
}
