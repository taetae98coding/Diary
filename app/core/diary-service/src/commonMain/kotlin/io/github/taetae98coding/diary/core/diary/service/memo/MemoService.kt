package io.github.taetae98coding.diary.core.diary.service.memo

import io.github.taetae98coding.diary.common.model.memo.MemoAndTagIdsEntity
import io.github.taetae98coding.diary.common.model.memo.MemoEntity
import io.github.taetae98coding.diary.core.diary.service.DiaryServiceModule
import io.github.taetae98coding.diary.core.diary.service.ext.getOrThrow
import io.github.taetae98coding.diary.core.diary.service.mapper.toDto
import io.github.taetae98coding.diary.core.diary.service.mapper.toEntity
import io.github.taetae98coding.diary.core.model.memo.MemoAndTagIds
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.model.memo.MemoDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
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
	public suspend fun update(memoId: String, detail: MemoDetail) {
		client
			.put("/memo/$memoId/update") {
				contentType(ContentType.Application.Json)
				setBody(detail.toEntity())
			}.getOrThrow<Unit>()
	}

	public suspend fun upsert(list: List<MemoAndTagIds>) {
		client
			.post("/memo/upsert") {
				contentType(ContentType.Application.Json)
				setBody(list.map(MemoAndTagIds::toEntity))
			}.getOrThrow<Unit>()
	}

	public suspend fun fetch(updateAt: Instant): List<MemoAndTagIds> {
		val response = client
			.get("/memo/fetch") {
				parameter("updateAt", updateAt)
			}.getOrThrow<List<MemoAndTagIdsEntity>>()

		return response.map {
			MemoAndTagIds(it.toDto(), it.tagIds)
		}
	}

	public suspend fun updateFinish(memoId: String, isFinish: Boolean) {
		client
			.put("/memo/$memoId/updateFinish") {
				parameter("isFinish", isFinish)
			}.getOrThrow<Unit>()
	}

	public suspend fun updateDelete(memoId: String, isDelete: Boolean) {
		client
			.put("/memo/$memoId/updateDelete") {
				parameter("isDelete", isDelete)
			}.getOrThrow<Unit>()
	}

	public suspend fun findById(id: String): MemoDto? {
		val response = client.get("/memo/$id").getOrThrow<MemoEntity?>()

		return response?.toDto()
	}
}
