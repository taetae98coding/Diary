package io.github.taetae98coding.diary.domain.buddy.usecase

import io.github.taetae98coding.diary.core.model.BuddyGroupAndBuddyIds
import io.github.taetae98coding.diary.domain.buddy.repository.BuddyRepository
import io.github.taetae98coding.diary.domain.fcm.repository.FCMRepository
import kotlinx.coroutines.flow.firstOrNull
import org.koin.core.annotation.Factory

@Factory
public class UpsertBuddyGroupUseCase internal constructor(
	private val buddyRepository: BuddyRepository,
	private val fcmRepository: FCMRepository,
) {
	public suspend operator fun invoke(
		buddyGroupAndBuddyIds: BuddyGroupAndBuddyIds,
		requester: String,
	): Result<Unit> = runCatching {
		val buddyGroup = buddyRepository.findGroupById(buddyGroupAndBuddyIds.buddyGroup.id).firstOrNull()

		buddyRepository.upsert(buddyGroupAndBuddyIds)
		buddyGroupAndBuddyIds.buddyIds
			.filter { it != requester }
			.forEach {
				if (buddyGroup == null) {
					fcmRepository.send(it, "그룹 초대", "${buddyGroupAndBuddyIds.buddyGroup.title}에 초대됐습니다.")
				} else {
					fcmRepository.send(it, "그룹 업데이트", "${buddyGroup.title} 업데이트됐습니다.")
				}
			}
	}
}
