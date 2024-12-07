package io.github.taetae98coding.diary.core.diary.service.buddy

import io.github.taetae98coding.diary.common.model.buddy.BuddyEntity
import io.github.taetae98coding.diary.common.model.buddy.BuddyGroupEntity
import io.github.taetae98coding.diary.common.model.request.buddy.UpsertBuddyGroupRequest
import io.github.taetae98coding.diary.core.diary.service.DiaryServiceModule
import io.github.taetae98coding.diary.core.diary.service.ext.getOrThrow
import io.github.taetae98coding.diary.core.model.buddy.Buddy
import io.github.taetae98coding.diary.core.model.buddy.BuddyGroup
import io.github.taetae98coding.diary.core.model.buddy.BuddyGroupDetail
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
public class BuddyService internal constructor(
	@Named(DiaryServiceModule.DIARY_CLIENT)
	private val client: HttpClient,
) {
	public suspend fun upsert(
		buddyGroup: BuddyGroup,
		buddyIds: Set<String>,
	) {
		client
			.post("/buddy/group/upsert") {
				val body = UpsertBuddyGroupRequest(
					buddyGroup = BuddyGroupEntity(
						id = buddyGroup.id,
						title = buddyGroup.detail.title,
						description = buddyGroup.detail.description,
					),
					buddyIds = buddyIds,
				)

				setBody(body)
				contentType(ContentType.Application.Json)
			}.getOrThrow<Unit>()
	}

	public suspend fun findBuddyGroup(): List<BuddyGroup> {
		val response = client.get("/buddy/group/find").getOrThrow<List<BuddyGroupEntity>>()

		return response.map {
			BuddyGroup(
				id = it.id,
				detail = BuddyGroupDetail(
					title = it.title,
					description = it.description,
				),
			)
		}
	}

	public suspend fun findBuddyByEmail(email: String): List<Buddy> {
		val response = client
			.get("/buddy/find") {
				parameter("email", email)
			}.getOrThrow<List<BuddyEntity>>()

		return response.map {
			Buddy(
				uid = it.uid,
				email = it.email,
			)
		}
	}
}
