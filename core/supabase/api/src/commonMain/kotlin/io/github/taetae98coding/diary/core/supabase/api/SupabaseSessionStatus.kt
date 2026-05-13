package io.github.taetae98coding.diary.core.supabase.api

public sealed interface SupabaseSessionStatus {
    public data class Authenticated(
        val userId: String,
        val email: String?,
    ) : SupabaseSessionStatus

    public data class NotAuthenticated(
        val userId: String?,
        val email: String?,
    ) : SupabaseSessionStatus {
        public companion object {
            public val NotLogin: NotAuthenticated = NotAuthenticated(userId = null, email = null)
        }
    }

    public data object Loading : SupabaseSessionStatus
}
