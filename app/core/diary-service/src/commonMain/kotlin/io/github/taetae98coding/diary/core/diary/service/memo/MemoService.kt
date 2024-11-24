package io.github.taetae98coding.diary.core.diary.service.memo

import io.github.taetae98coding.diary.common.model.memo.MemoEntity
import io.github.taetae98coding.diary.core.diary.service.DiaryServiceModule
import io.github.taetae98coding.diary.core.diary.service.ext.getOrThrow
import io.github.taetae98coding.diary.core.model.memo.MemoAndTagIds
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
	public suspend fun upsert(list: List<MemoAndTagIds>) {
		client
			.post("/memo/upsert") {
				val body =
					list.map {
						MemoEntity(
							id = it.memo.id,
							title = it.memo.detail.title,
							description = it.memo.detail.description,
							start = it.memo.detail.start,
							endInclusive = it.memo.detail.endInclusive,
							color = it.memo.detail.color,
							owner = requireNotNull(it.memo.owner),
							primaryTag = it.memo.primaryTag,
							tagIds = it.tagIds,
							isFinish = it.memo.isFinish,
							isDelete = it.memo.isDelete,
							updateAt = it.memo.updateAt,
						)
					}

				contentType(ContentType.Application.Json)
				setBody(body)
			}.getOrThrow<Unit>()
	}

	public suspend fun fetch(updateAt: Instant): List<MemoAndTagIds> {
		val response =
			client
				.get("/memo/fetch") {
					parameter("updateAt", updateAt)
				}.getOrThrow<List<MemoEntity>>()

		return response.map {
			val dto =
				MemoDto(
					id = it.id,
					detail =
						MemoDetail(
							title = it.title,
							description = it.description,
							start = it.start,
							endInclusive = it.endInclusive,
							color = it.color,
						),
					owner = it.owner,
					primaryTag = it.primaryTag,
					isFinish = it.isFinish,
					isDelete = it.isDelete,
					updateAt = it.updateAt,
					serverUpdateAt = it.updateAt,
				)

			MemoAndTagIds(dto, it.tagIds)
		}
	}
}
