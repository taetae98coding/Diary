package io.github.taetae98coding.diary.core.diary.service.fcm

import io.github.taetae98coding.diary.common.model.request.fcm.DeleteFCMRequest
import io.github.taetae98coding.diary.common.model.request.fcm.UpsertFCMRequest
import io.github.taetae98coding.diary.core.diary.service.DiaryServiceModule
import io.github.taetae98coding.diary.core.diary.service.ext.getOrThrow
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
public class FCMService internal constructor(
    @Named(DiaryServiceModule.DIARY_CLIENT)
    private val client: HttpClient,
) {
    public suspend fun upsert(token: String) {
        return client.post("/fcm/upsert") {
            contentType(ContentType.Application.Json)
            setBody(UpsertFCMRequest(token))
        }.getOrThrow()
    }

    public suspend fun delete(token: String) {
        return client.post("/fcm/delete") {
            contentType(ContentType.Application.Json)
            setBody(DeleteFCMRequest(token))
        }.getOrThrow()
    }
}
