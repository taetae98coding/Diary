package io.github.taetae98coding.diary.feature.tag

import io.github.taetae98coding.diary.common.model.response.DiaryResponse
import io.github.taetae98coding.diary.common.model.tag.TagEntity
import io.github.taetae98coding.diary.core.model.Tag
import io.github.taetae98coding.diary.domain.tag.usecase.FetchTagUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.UpsertTagUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Instant
import org.koin.ktor.plugin.scope

public fun Route.tagRouting() {
	route("/tag") {
		authenticate("account") {
			post<List<TagEntity>>("/upsert") { request ->
				val principal = call.principal<JWTPrincipal>()
				if (principal == null) {
					call.respond(HttpStatusCode.Unauthorized, DiaryResponse.Unauthorized)
					return@post
				}

				val useCase = call.scope.get<UpsertTagUseCase>()
				val memoList = request.map(TagEntity::toTag)

				useCase(memoList)
					.onSuccess { call.respond(DiaryResponse.Success) }
					.onFailure { call.respond(DiaryResponse.InternalServerError) }
			}

			get("/fetch") {
				val principal = call.principal<JWTPrincipal>()
				if (principal == null) {
					call.respond(HttpStatusCode.Unauthorized, DiaryResponse.Unauthorized)
					return@get
				}

				val uid = principal.payload.getClaim("uid").asString()
				val updateAt = call.parameters["updateAt"]?.let { Instant.parse(it) } ?: return@get
				val useCase = call.scope.get<FetchTagUseCase>()

				useCase(uid, updateAt)
					.first()
					.onSuccess { call.respond(DiaryResponse.success(it.map(Tag::toEntity))) }
					.onFailure { call.respond(DiaryResponse.InternalServerError) }
			}
		}
	}
}

private fun TagEntity.toTag(): Tag =
	Tag(
		id = id,
		title = title,
		description = description,
		color = color,
		owner = owner,
		isFinish = isFinish,
		isDelete = isDelete,
		updateAt = updateAt,
	)

private fun Tag.toEntity(): TagEntity =
	TagEntity(
		id = id,
		title = title,
		description = description,
		color = color,
		owner = owner,
		isFinish = isFinish,
		isDelete = isDelete,
		updateAt = updateAt,
	)
