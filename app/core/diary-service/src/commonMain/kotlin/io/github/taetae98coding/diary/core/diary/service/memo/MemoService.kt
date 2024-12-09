package io.github.taetae98coding.diary.core.diary.service.memo

import io.github.taetae98coding.diary.common.model.memo.LegacyMemoEntity
import io.github.taetae98coding.diary.core.diary.service.DiaryServiceModule
import io.github.taetae98coding.diary.core.diary.service.ext.getOrThrow
import io.github.taetae98coding.diary.core.diary.service.mapper.toDto
import io.github.taetae98coding.diary.core.diary.service.mapper.toEntity
import io.github.taetae98coding.diary.core.model.memo.MemoAndTagIds
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.datetime.Instant
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
public class MemoService internal constructor(
    @Named(DiaryServiceModule.DIARY_CLIENT)
    private val client: HttpClient,
) {
    public suspend fun upsert(list: List<MemoAndTagIds>) {
        client.post("/memo/upsert") {
            contentType(ContentType.Application.Json)
            setBody(list.map(MemoAndTagIds::toEntity))
        }.getOrThrow<Unit>()
    }

    public suspend fun fetch(updateAt: Instant): List<MemoAndTagIds> {
        val response = client.get("/memo/fetch") {
            parameter("updateAt", updateAt)
        }.getOrThrow<List<LegacyMemoEntity>>()

        return response.map {
            MemoAndTagIds(it.toDto(), it.tagIds)
        }
    }
}
