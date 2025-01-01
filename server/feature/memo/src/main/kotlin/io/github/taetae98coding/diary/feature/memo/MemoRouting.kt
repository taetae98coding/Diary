package io.github.taetae98coding.diary.feature.memo

import io.github.taetae98coding.diary.common.model.memo.MemoAndTagIdsEntity
import io.github.taetae98coding.diary.common.model.memo.MemoDetailEntity
import io.github.taetae98coding.diary.common.model.memo.MemoEntity
import io.github.taetae98coding.diary.common.model.response.DiaryResponse
import io.github.taetae98coding.diary.core.model.Memo
import io.github.taetae98coding.diary.core.model.MemoAndTagIds
import io.github.taetae98coding.diary.core.model.MemoDetail
import io.github.taetae98coding.diary.domain.memo.usecase.DeleteMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.FetchMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.FindMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.FinishMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.RestartMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.UpdateMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.UpsertMemoUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Instant
import org.koin.ktor.plugin.scope

public fun Route.memoRouting() {
    route("/memo") {
        authenticate("account") {
            post<List<MemoAndTagIdsEntity>>("/upsert") { request ->
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
                    .onFailure { call.respond(HttpStatusCode.InternalServerError, DiaryResponse.InternalServerError) }
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
                        call.respond(DiaryResponse.success(list.map { it.toEntity() }))
                    }.onFailure { call.respond(HttpStatusCode.InternalServerError, DiaryResponse.InternalServerError) }
            }

            get("/{id}") {
                val principal = call.principal<JWTPrincipal>()
                if (principal == null) {
                    call.respond(HttpStatusCode.Unauthorized, DiaryResponse.Unauthorized)
                    return@get
                }

                val id = call.parameters["id"]
                val useCase = call.scope.get<FindMemoUseCase>()

                useCase(id)
                    .first()
                    .onSuccess { memo ->
                        val entity = memo?.let {
                            MemoEntity(
                                id = it.id,
                                title = it.title,
                                description = it.description,
                                start = it.start,
                                endInclusive = it.endInclusive,
                                color = it.color,
                                primaryTag = it.primaryTag,
                                isFinish = it.isFinish,
                                isDelete = it.isDelete,
                                updateAt = it.updateAt,
                            )
                        }
                        call.respond(DiaryResponse.success(entity))
                    }.onFailure { call.respond(HttpStatusCode.InternalServerError, DiaryResponse.InternalServerError) }
            }

            put<MemoDetailEntity>("/{id}/update") { request ->
                val principal = call.principal<JWTPrincipal>()
                if (principal == null) {
                    call.respond(HttpStatusCode.Unauthorized, DiaryResponse.Unauthorized)
                    return@put
                }

                val uid = principal.payload.getClaim("uid").asString()
                val id = call.parameters["id"]
                val useCase = call.scope.get<UpdateMemoUseCase>()

                useCase(id, request.toDetail(), uid)
                    .onSuccess { call.respond(DiaryResponse.Success) }
                    .onFailure { call.respond(HttpStatusCode.InternalServerError, DiaryResponse.InternalServerError) }
            }

            put("/{id}/updateDelete") {
                val principal = call.principal<JWTPrincipal>()
                if (principal == null) {
                    call.respond(HttpStatusCode.Unauthorized, DiaryResponse.Unauthorized)
                    return@put
                }

                val uid = principal.payload.getClaim("uid").asString()
                val id = call.parameters["id"]
                val isDelete = call.parameters["isDelete"]?.toBoolean() ?: run {
                    call.respond(HttpStatusCode.BadRequest, DiaryResponse.BadRequest)
                    return@put
                }

                if (isDelete) {
                    val useCase = call.scope.get<DeleteMemoUseCase>()

                    useCase(id, uid)
                        .onSuccess { call.respond(DiaryResponse.Success) }
                        .onFailure { call.respond(HttpStatusCode.InternalServerError,DiaryResponse.InternalServerError) }
                }
            }

            put("/{id}/updateFinish") {
                val principal = call.principal<JWTPrincipal>()
                if (principal == null) {
                    call.respond(HttpStatusCode.Unauthorized, DiaryResponse.Unauthorized)
                    return@put
                }

                val uid = principal.payload.getClaim("uid").asString()
                val id = call.parameters["id"]
                val isFinish = call.parameters["isFinish"]?.toBoolean() ?: run {
                    call.respond(HttpStatusCode.BadRequest,DiaryResponse.BadRequest)
                    return@put
                }

                if (isFinish) {
                    val useCase = call.scope.get<FinishMemoUseCase>()

                    useCase(id, uid)
                        .onSuccess { call.respond(DiaryResponse.Success) }
                        .onFailure { call.respond(HttpStatusCode.InternalServerError,DiaryResponse.InternalServerError) }
                } else {
                    val useCase = call.scope.get<RestartMemoUseCase>()

                    useCase(id, uid)
                        .onSuccess { call.respond(DiaryResponse.Success) }
                        .onFailure { call.respond(HttpStatusCode.InternalServerError,DiaryResponse.InternalServerError) }
                }
            }
        }
    }
}

private fun MemoAndTagIdsEntity.toMemo(): Memo =
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

private fun MemoDetailEntity.toDetail(): MemoDetail = MemoDetail(
    title = title,
    description = description,
    start = start,
    endInclusive = endInclusive,
    color = color,
)

private fun MemoAndTagIds.toEntity(): MemoAndTagIdsEntity =
    MemoAndTagIdsEntity(
        id = memo.id,
        title = memo.title,
        description = memo.description,
        start = memo.start,
        endInclusive = memo.endInclusive,
        color = memo.color,
        primaryTag = memo.primaryTag,
        tagIds = tagIds,
        isFinish = memo.isFinish,
        isDelete = memo.isDelete,
        updateAt = memo.updateAt,
    )
