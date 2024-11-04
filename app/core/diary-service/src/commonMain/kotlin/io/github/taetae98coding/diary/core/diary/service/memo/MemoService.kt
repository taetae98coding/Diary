package io.github.taetae98coding.diary.core.diary.service.memo

import io.github.taetae98coding.diary.common.model.memo.MemoEntity
import io.github.taetae98coding.diary.core.diary.service.DiaryServiceModule
import io.github.taetae98coding.diary.core.diary.service.ext.getOrThrow
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.model.memo.MemoDto
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
    public suspend fun upsert(list: List<Memo>) {
        return client.post("/memo/upsert") {
            val body = list.map {
                MemoEntity(
                    id = it.id,
                    title = it.detail.title,
                    description = it.detail.description,
                    start = it.detail.start,
                    endInclusive = it.detail.endInclusive,
                    color = it.detail.color,
                    owner = requireNotNull(it.owner),
                    isFinish = it.isFinish,
                    isDelete = it.isDelete,
                    updateAt = it.updateAt,
                )
            }

            contentType(ContentType.Application.Json)
            setBody(body)
        }.getOrThrow()
    }

    public suspend fun fetch(updateAt: Instant): List<MemoDto> {
        val response = client.get("/memo/fetch") {
            parameter("updateAt", updateAt)
        }.getOrThrow<List<MemoEntity>>()

        return response.map {
            MemoDto(
                id = it.id,
                detail = MemoDetail(
                    title = it.title,
                    description = it.description,
                    start = it.start,
                    endInclusive = it.endInclusive,
                    color = it.color,
                ),
                owner = it.owner,
                isFinish = it.isFinish,
                isDelete = it.isDelete,
                updateAt = it.updateAt,
                serverUpdateAt = it.updateAt,
            )
        }
    }
}
