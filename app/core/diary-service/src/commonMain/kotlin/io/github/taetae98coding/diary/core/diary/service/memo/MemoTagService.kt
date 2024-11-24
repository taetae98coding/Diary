package io.github.taetae98coding.diary.core.diary.service.memo

import io.github.taetae98coding.diary.common.model.memo.MemoTagEntity
import io.github.taetae98coding.diary.core.diary.service.DiaryServiceModule
import io.github.taetae98coding.diary.core.diary.service.ext.getOrThrow
import io.github.taetae98coding.diary.core.model.memo.MemoTagDto
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
public class MemoTagService internal constructor(
    @Named(DiaryServiceModule.DIARY_CLIENT)
    private val client: HttpClient,
) {
    public suspend fun upsert(list: List<MemoTagDto>) {
        return client.post("/memoTag/upsert") {
            val body = list.map {
                MemoTagEntity(
                    memoId = it.memoId,
                    tagId = it.tagId,
                    isSelected = it.isSelected,
                    updateAt = it.updateAt,
                )
            }

            contentType(ContentType.Application.Json)
            setBody(body)
        }.getOrThrow()
    }
}
