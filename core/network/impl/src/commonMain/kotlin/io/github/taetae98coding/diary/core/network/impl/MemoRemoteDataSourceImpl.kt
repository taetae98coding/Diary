package io.github.taetae98coding.diary.core.network.impl

import io.github.taetae98coding.diary.core.network.api.datasource.MemoRemoteDataSource
import io.github.taetae98coding.diary.core.network.api.entity.MemoRemoteEntity
import io.github.taetae98coding.diary.core.supabase.api.SupabaseFunctions
import io.ktor.client.call.body
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.koin.core.annotation.Factory

@Factory
public class MemoRemoteDataSourceImpl(private val supabaseFunctions: SupabaseFunctions) : MemoRemoteDataSource {
    override suspend fun push(memoList: List<MemoRemoteEntity>) {
        supabaseFunctions(
            function = "v1-memo-push",
            body = buildJsonObject {
                put("memoList", Json.parseToJsonElement(Json.encodeToString(memoList)))
            },
            headers = Headers.build {
                append(HttpHeaders.ContentType, "application/json")
            },
        )
    }

    override suspend fun pull(updatedAt: Long): List<MemoRemoteEntity> {
        val response = supabaseFunctions(
            function = "v1-memo-pull",
            body = buildJsonObject {
                put("updatedAt", updatedAt)
            },
            headers = Headers.build {
                append(HttpHeaders.ContentType, "application/json")
            },
        )

        return response.body()
    }
}
