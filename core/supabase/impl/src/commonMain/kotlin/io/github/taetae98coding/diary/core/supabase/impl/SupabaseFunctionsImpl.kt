package io.github.taetae98coding.diary.core.supabase.impl

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.functions.functions
import io.github.taetae98coding.diary.core.supabase.api.SupabaseFunctions
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import kotlinx.serialization.json.JsonObject
import org.koin.core.annotation.Factory

@Factory
internal class SupabaseFunctionsImpl(private val supabaseClient: SupabaseClient) : SupabaseFunctions {
    override suspend fun invoke(
        function: String,
        builder: HttpRequestBuilder.() -> Unit,
    ): HttpResponse {
        return supabaseClient.functions(function = function)
    }

    override suspend fun invoke(
        function: String,
        body: JsonObject,
        headers: Headers,
    ): HttpResponse {
        return supabaseClient.functions(function = function, body = body, headers = headers)
    }
}
