package io.github.taetae98coding.diary.core.network.impl.datasource

import io.github.taetae98coding.diary.core.network.api.datasource.RoutineRemoteDataSource
import io.github.taetae98coding.diary.core.network.api.entity.RoutineRemoteEntity
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
public class RoutineRemoteDataSourceImpl(
    @param:ApiJson
    private val json: Json,
    @param:Provided
    private val supabaseFunctions: SupabaseFunctions,
) : RoutineRemoteDataSource {
    override suspend fun push(routineList: List<RoutineRemoteEntity>) {
        supabaseFunctions(
            function = "v1-routine-push",
            body = buildJsonObject {
                put("routineList", json.encodeToJsonElement(routineList))
            },
            headers = Headers.build {
                append(HttpHeaders.ContentType, "application/json")
            },
        )
    }

    override suspend fun pull(updatedAt: Long): List<RoutineRemoteEntity> {
        val response = supabaseFunctions(
            function = "v1-routine-pull",
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
