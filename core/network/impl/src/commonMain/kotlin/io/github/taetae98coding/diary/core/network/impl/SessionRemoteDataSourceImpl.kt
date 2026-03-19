package io.github.taetae98coding.diary.core.network.impl

import io.github.taetae98coding.diary.core.network.api.datasource.SessionRemoteDataSource
import io.github.taetae98coding.diary.core.network.api.entity.SessionRemoteEntity
import io.github.taetae98coding.diary.core.supabase.api.SupabaseFunctions
import io.ktor.client.call.body
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.koin.core.annotation.Factory

@Factory
public class SessionRemoteDataSourceImpl(private val supabaseFunctions: SupabaseFunctions) : SessionRemoteDataSource {
    override suspend fun getByGoogleAuthorizationCode(
        clientId: String,
        code: String,
        redirectUri: String,
    ): SessionRemoteEntity {
        return supabaseFunctions(
            function = "v1-session-google-code",
            body = buildJsonObject {
                put("clientId", clientId)
                put("code", code)
                put("redirectUri", redirectUri)
            },
            headers = Headers.build {
                append(HttpHeaders.ContentType, "application/json")
            },
        ).body()
    }

    override suspend fun getByGoogleIdToken(idToken: String): SessionRemoteEntity {
        return supabaseFunctions(
            function = "v1-session-google-idToken",
            body = buildJsonObject {
                put("idToken", idToken)
            },
            headers = Headers.build {
                append(HttpHeaders.ContentType, "application/json")
            },
        ).body()
    }
}
