package io.github.taetae98coding.diary.core.supabase.api

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import kotlinx.serialization.json.JsonObject

public interface SupabaseFunctions {
    public suspend operator fun invoke(
        function: String,
        builder: HttpRequestBuilder.() -> Unit = {},
    ): HttpResponse

    public suspend operator fun invoke(
        function: String,
        body: JsonObject,
        headers: Headers,
    ): HttpResponse
}
