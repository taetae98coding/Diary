package io.github.taetae98coding.diary.domain.buddy.usecase

import io.github.taetae98coding.diary.core.model.Memo
import io.github.taetae98coding.diary.domain.account.repository.AccountRepository
import io.github.taetae98coding.diary.domain.buddy.repository.BuddyRepository
import io.github.taetae98coding.diary.domain.fcm.repository.FCMRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class UpsertBuddyGroupMemoUseCase internal constructor(
	private val buddyRepository: BuddyRepository,
	private val fcmRepository: FCMRepository,
	private val accountRepository: AccountRepository,
) {
	public suspend operator fun invoke(
		groupId: String,
		memo: Memo,
		tagIds: Set<String>,
		requesterUid: String,
	): Result<Unit> {
		return runCatching {
			// TODO Permission Check

			val account = accountRepository.findByUid(requesterUid).first() ?: return@runCatching

			buddyRepository.upsert(groupId, memo, tagIds)
			buddyRepository
				.findBuddyIdByGroupId(groupId)
				.first()
				.filter { it != requesterUid }
				.forEach {
					fcmRepository.send(it, "그룹 메모", "'${memo.title}' 메모가 추가됐습니다. (${account.email})")
				}
		}
	}
}
