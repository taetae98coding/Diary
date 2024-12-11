package io.github.taetae98coding.diary.feature.buddy

import io.github.taetae98coding.diary.common.model.buddy.BuddyEntity
import io.github.taetae98coding.diary.common.model.buddy.BuddyGroupEntity
import io.github.taetae98coding.diary.common.model.memo.LegacyMemoEntity
import io.github.taetae98coding.diary.common.model.memo.MemoEntity
import io.github.taetae98coding.diary.common.model.request.buddy.UpsertBuddyGroupRequest
import io.github.taetae98coding.diary.common.model.response.DiaryResponse
import io.github.taetae98coding.diary.core.model.BuddyGroup
import io.github.taetae98coding.diary.core.model.BuddyGroupAndBuddyIds
import io.github.taetae98coding.diary.core.model.Memo
import io.github.taetae98coding.diary.domain.buddy.usecase.FindBuddyGroupMemoByDate
import io.github.taetae98coding.diary.domain.buddy.usecase.FindBuddyGroupUseCase
import io.github.taetae98coding.diary.domain.buddy.usecase.FindBuddyUseCase
import io.github.taetae98coding.diary.domain.buddy.usecase.UpsertBuddyGroupMemoUseCase
import io.github.taetae98coding.diary.domain.buddy.usecase.UpsertBuddyGroupUseCase
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
import kotlinx.datetime.LocalDate
import org.koin.ktor.plugin.scope

public fun Route.buddyRouting() {
    route("/buddy") {
        authenticate("account") {
            get("/find") {
                val email = call.parameters["email"]
                val uid = call
                    .principal<JWTPrincipal>()
                    ?.payload
                    ?.getClaim("uid")
                    ?.toString()

                val useCase = call.scope.get<FindBuddyUseCase>()

                useCase(email, uid)
                    .first()
                    .onSuccess { list ->
                        list
                            .map {
                                BuddyEntity(
                                    uid = it.uid,
                                    email = it.email,
                                )
                            }.also {
                                call.respond(DiaryResponse.success(it))
                            }
                    }.onFailure { call.respond(DiaryResponse.InternalServerError) }
            }

            route("/group") {
                get("/find") {
                    val principal = call.principal<JWTPrincipal>()
                    if (principal == null) {
                        call.respond(HttpStatusCode.Unauthorized, DiaryResponse.Unauthorized)
                        return@get
                    }

                    val uid = principal.payload.getClaim("uid").asString()
                    val useCase = call.scope.get<FindBuddyGroupUseCase>()

                    useCase(uid)
                        .first()
                        .onSuccess { list ->
                            list
                                .map {
                                    BuddyGroupEntity(
                                        id = it.id,
                                        title = it.title,
                                        description = it.description,
                                    )
                                }.also {
                                    call.respond(DiaryResponse.success(it))
                                }
                        }.onFailure { call.respond(DiaryResponse.InternalServerError) }
                }

                post<UpsertBuddyGroupRequest>("/upsert") { request ->
                    val principal = call.principal<JWTPrincipal>()
                    if (principal == null) {
                        call.respond(HttpStatusCode.Unauthorized, DiaryResponse.Unauthorized)
                        return@post
                    }

                    val uid = principal.payload.getClaim("uid").asString()
                    val buddyGroupAndBuddyIds = BuddyGroupAndBuddyIds(
                        buddyGroup = BuddyGroup(
                            id = request.buddyGroup.id,
                            title = request.buddyGroup.title,
                            description = request.buddyGroup.description,
                        ),
                        buddyIds = request.buddyIds,
                    )

                    val useCase = call.scope.get<UpsertBuddyGroupUseCase>()

                    useCase(buddyGroupAndBuddyIds, uid)
                        .onSuccess { call.respond(DiaryResponse.Success) }
                        .onFailure { call.respond(DiaryResponse.InternalServerError) }
                }

                post<LegacyMemoEntity>("/{groupId}/upsertMemo") { request ->
                    val principal = call.principal<JWTPrincipal>()
                    if (principal == null) {
                        call.respond(HttpStatusCode.Unauthorized, DiaryResponse.Unauthorized)
                        return@post
                    }

                    val uid = principal.payload.getClaim("uid").asString()

                    val groupId = call.parameters["groupId"] ?: run {
                        call.respond(HttpStatusCode.BadRequest, DiaryResponse.BadRequest)
                        return@post
                    }

                    val useCase = call.scope.get<UpsertBuddyGroupMemoUseCase>()

                    useCase(groupId, request.toMemo(), request.tagIds, uid)
                        .onSuccess { call.respond(DiaryResponse.Success) }
                        .onFailure { call.respond(DiaryResponse.InternalServerError) }
                }

                get("/{groupId}/findMemoByDateRange") {
                    val groupId = call.parameters["groupId"] ?: run {
                        call.respond(HttpStatusCode.BadRequest, DiaryResponse.BadRequest)
                        return@get
                    }
                    val start = call.parameters["start"]?.let(LocalDate::parse) ?: run {
                        call.respond(HttpStatusCode.BadRequest, DiaryResponse.BadRequest)
                        return@get
                    }
                    val endInclusive = call.parameters["endInclusive"]?.let(LocalDate::parse) ?: run {
                        call.respond(HttpStatusCode.BadRequest, DiaryResponse.BadRequest)
                        return@get
                    }
                    val useCase = call.scope.get<FindBuddyGroupMemoByDate>()

                    useCase(groupId, start..endInclusive)
                        .first()
                        .onSuccess { list ->
                            val body = list.map {
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

                            call.respond(DiaryResponse.success(body))
                        }.onFailure {
                            call.respond(DiaryResponse.InternalServerError)
                        }
                }
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
