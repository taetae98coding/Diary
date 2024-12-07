package io.github.taetae98coding.diary.core.diary.service.memo

import io.github.taetae98coding.diary.common.model.tag.TagEntity
import io.github.taetae98coding.diary.core.diary.service.DiaryServiceModule
import io.github.taetae98coding.diary.core.diary.service.ext.getOrThrow
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.github.taetae98coding.diary.core.model.tag.TagDto
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
public class TagService internal constructor(
	@Named(DiaryServiceModule.DIARY_CLIENT)
	private val client: HttpClient,
) {
	public suspend fun upsert(list: List<TagDto>) {
		client
			.post("/tag/upsert") {
				val body =
					list.map {
						TagEntity(
							id = it.id,
							title = it.detail.title,
							description = it.detail.description,
							color = it.detail.color,
							owner = requireNotNull(it.owner),
							isFinish = it.isFinish,
							isDelete = it.isDelete,
							updateAt = it.updateAt,
						)
					}

				contentType(ContentType.Application.Json)
				setBody(body)
			}.getOrThrow<Unit>()
	}

	public suspend fun fetch(updateAt: Instant): List<TagDto> {
		val response =
			client
				.get("/tag/fetch") {
					parameter("updateAt", updateAt)
				}.getOrThrow<List<TagEntity>>()

		return response.map {
			TagDto(
				id = it.id,
				detail =
					TagDetail(
						title = it.title,
						description = it.description,
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
