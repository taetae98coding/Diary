package io.github.taetae98coding.diary.core.network.impl.datasource

import io.github.taetae98coding.diary.core.network.api.datasource.MemoRemoteDataSource
import io.github.taetae98coding.diary.core.network.api.entity.MemoRemoteEntity
import io.github.taetae98coding.diary.core.network.impl.di.ApiJson
import io.github.taetae98coding.diary.core.supabase.api.SupabaseFunctions
import io.ktor.client.call.body
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.put
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class MemoRemoteDataSourceImpl(
    @param:ApiJson
    private val json: Json,
    @param:Provided
    private val supabaseFunctions: SupabaseFunctions,
) : MemoRemoteDataSource {
    override suspend fun push(memoList: List<MemoRemoteEntity>) {
        supabaseFunctions(
            function = "v2-memo-push",
            body = buildJsonObject {
                put("memoList", json.encodeToJsonElement(memoList))
            },
            headers = Headers.build {
                append(HttpHeaders.ContentType, "application/json")
            },
        )
    }

    override suspend fun pull(updatedAt: Long): List<MemoRemoteEntity> {
        val response = supabaseFunctions(
            function = "v2-memo-pull",
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
