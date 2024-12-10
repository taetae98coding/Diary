package io.github.taetae98coding.diary.feature.memo

import io.github.taetae98coding.diary.common.model.memo.LegacyMemoEntity
import io.github.taetae98coding.diary.common.model.response.DiaryResponse
import io.github.taetae98coding.diary.core.model.MemoAndTagIds
import io.github.taetae98coding.diary.core.model.Memo
import io.github.taetae98coding.diary.domain.memo.usecase.FetchMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.UpsertMemoUseCase
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

public fun Route.memoRouting() {
    route("/memo") {
        authenticate("account") {
            post<List<LegacyMemoEntity>>("/upsert") { request ->
                val principal = call.principal<JWTPrincipal>()
                if (principal == null) {
                    call.respond(HttpStatusCode.Unauthorized, DiaryResponse.Unauthorized)
                    return@post
                }

                val useCase = call.scope.get<UpsertMemoUseCase>()
                val memoList =
                    request.map {
                        MemoAndTagIds(
                            memo = it.toMemo(),
                            tagIds = it.tagIds,
                        )
                    }

                useCase(memoList, principal.payload.getClaim("uid").asString())
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
                val useCase = call.scope.get<FetchMemoUseCase>()

                useCase(uid, updateAt)
                    .first()
                    .onSuccess { list ->
                        call.respond(DiaryResponse.success(list.map { it.toEntity(uid) }))
                    }
                    .onFailure { call.respond(DiaryResponse.InternalServerError) }
            }
        }
    }
}

private fun LegacyMemoEntity.toMemo(): Memo =
    Memo(
        id = id,
        title = title,
        description = description,
        start = start,
        endInclusive = endInclusive,
        color = color,
        primaryTag = primaryTag,
        isFinish = isFinish,
        isDelete = isDelete,
        updateAt = updateAt,
    )

private fun MemoAndTagIds.toEntity(owner: String): LegacyMemoEntity =
    LegacyMemoEntity(
        id = memo.id,
        title = memo.title,
        description = memo.description,
        start = memo.start,
        endInclusive = memo.endInclusive,
        color = memo.color,
        owner = owner,
        primaryTag = memo.primaryTag,
        tagIds = tagIds,
        isFinish = memo.isFinish,
        isDelete = memo.isDelete,
        updateAt = memo.updateAt,
    )
