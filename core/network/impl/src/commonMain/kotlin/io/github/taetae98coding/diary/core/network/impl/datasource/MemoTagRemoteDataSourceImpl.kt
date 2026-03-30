package io.github.taetae98coding.diary.core.network.impl.datasource

import io.github.taetae98coding.diary.core.network.api.datasource.MemoTagRemoteDataSource
import io.github.taetae98coding.diary.core.network.api.entity.MemoTagRemoteEntity
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

@Factory
public class MemoTagRemoteDataSourceImpl(
    @param:ApiJson
    private val json: Json,
    private val supabaseFunctions: SupabaseFunctions,
) : MemoTagRemoteDataSource {
    override suspend fun push(memoTagList: List<MemoTagRemoteEntity>) {
        supabaseFunctions(
            function = "v1-memo-tag-push",
            body = buildJsonObject {
                put("memoTagList", json.encodeToJsonElement(memoTagList))
            },
            headers = Headers.build {
                append(HttpHeaders.ContentType, "application/json")
            },
        )
    }

    override suspend fun pull(updatedAt: Long): List<MemoTagRemoteEntity> {
        val response = supabaseFunctions(
            function = "v1-memo-tag-pull",
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
