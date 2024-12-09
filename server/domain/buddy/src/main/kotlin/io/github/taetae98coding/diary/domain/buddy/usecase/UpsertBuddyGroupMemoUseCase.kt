package io.github.taetae98coding.diary.domain.buddy.usecase

import io.github.taetae98coding.diary.core.model.Memo
import io.github.taetae98coding.diary.domain.buddy.repository.BuddyRepository
import io.github.taetae98coding.diary.domain.fcm.repository.FCMRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class UpsertBuddyGroupMemoUseCase internal constructor(
    private val buddyRepository: BuddyRepository,
    private val fcmRepository: FCMRepository,
) {
    public suspend operator fun invoke(
        groupId: String,
        memo: Memo,
        tagIds: Set<String>,
    ): Result<Unit> {
        return runCatching {
            // TODO Permission Check

            buddyRepository.upsert(groupId, memo, tagIds)
            buddyRepository.findBuddyIdByGroupId(groupId).first()
                .forEach {
                    fcmRepository.send(it, "그룹 메모", "${memo.title}가 추가되었습니다.")
                }
        }
    }
}
